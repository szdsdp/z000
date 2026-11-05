package com.szd.z000.base.common.excel;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientAjaxResult;
import com.szd.core.client.exception.ClientCustomException;
import com.szd.core.client.util.ClientUtilConvert;
import com.szd.core.client.util.ClientUtilDate;
import com.szd.core.client.util.ClientUtilSpring;
import com.szd.z000.base.common.util.ReflectUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Excel相关处理
 */
public class ExcelUtil<T> {
    private static final Logger log = LoggerFactory.getLogger(ExcelUtil.class);
    public static final int SHEET_SIZE = 65536;

    public ClientCore clientCore;
    private String sheetName;
    private ExcelExport.Type type;
    private Workbook wb;
    private Sheet sheet;
    private Map<String, CellStyle> styles;
    private List<T> list;
    private List<Object[]> fields;
    public Class<T> clazz;

    public ExcelUtil(Class<T> clazz) {
        this.clazz = clazz;
        this.clientCore = ClientUtilSpring.getBean(ClientCore.class);
    }

    public void init(List<T> list, String sheetName, ExcelExport.Type type) {
        if (list == null) {
            list = new ArrayList<T>();
        }
        this.list = list;
        this.sheetName = sheetName;
        this.type = type;
        createExcelField();
        createWorkbook();
    }

    public List<T> importExcel(InputStream is) throws Exception {
        return importExcel(StringUtils.EMPTY, is);
    }

    public List<T> importExcel(String sheetName, InputStream is) throws Exception {
        this.type = ExcelExport.Type.IMPORT;
        this.wb = WorkbookFactory.create(is);
        List<T> list = new ArrayList<T>();
        Sheet sheet = null;
        if (StringUtils.isNotEmpty(sheetName)) {
            sheet = wb.getSheet(sheetName);
        } else {
            sheet = wb.getSheetAt(0);
        }
        if (sheet == null) {
            throw new IOException("文件sheet不存在");
        }
        int rows = sheet.getPhysicalNumberOfRows();
        if (rows > 0) {
            Map<String, Integer> cellMap = new HashMap<String, Integer>();
            Row heard = sheet.getRow(0);
            for (int i = 0; i < heard.getPhysicalNumberOfCells(); i++) {
                Cell cell = heard.getCell(i);
                if (cell != null) {
                    String value = this.getCellValue(heard, i).toString();
                    cellMap.put(value, i);
                } else {
                    cellMap.put(null, i);
                }
            }
            Field[] allFields = clazz.getDeclaredFields();
            Map<Integer, Field> fieldsMap = new HashMap<Integer, Field>();
            for (int col = 0; col < allFields.length; col++) {
                Field field = allFields[col];
                ExcelExport attr = field.getAnnotation(ExcelExport.class);
                if (attr != null && (attr.type() == ExcelExport.Type.ALL || attr.type() == type)) {
                    field.setAccessible(true);
                    Integer column = cellMap.get(attr.name());
                    if (column != null) {
                        fieldsMap.put(column, field);
                    }
                }
            }
            for (int i = 1; i < rows; i++) {
                Row row = sheet.getRow(i);
                if (isRowEmpty(row)) {
                    continue;
                }
                T entity = null;
                for (Map.Entry<Integer, Field> entry : fieldsMap.entrySet()) {
                    Object val = this.getCellValue(row, entry.getKey());
                    entity = (entity == null ? clazz.newInstance() : entity);
                    Field field = fieldsMap.get(entry.getKey());
                    Class<?> fieldType = field.getType();
                    if (String.class == fieldType) {
                        String s = ClientUtilConvert.toStr(val);
                        if (StringUtils.endsWith(s, ".0")) {
                            val = StringUtils.substringBefore(s, ".0");
                        } else {
                            val = ClientUtilConvert.toStr(val);
                        }
                    } else if ((Integer.TYPE == fieldType) || (Integer.class == fieldType)) {
                        val = ClientUtilConvert.toInt(val);
                    } else if ((Long.TYPE == fieldType) || (Long.class == fieldType)) {
                        val = ClientUtilConvert.toLong(val);
                    } else if ((Double.TYPE == fieldType) || (Double.class == fieldType)) {
                        val = ClientUtilConvert.toDouble(val);
                    } else if ((Float.TYPE == fieldType) || (Float.class == fieldType)) {
                        val = ClientUtilConvert.toFloat(val);
                    } else if (BigDecimal.class == fieldType) {
                        val = ClientUtilConvert.toBigDecimal(val);
                    } else if (Date.class == fieldType) {
                        if (val instanceof String) {
                            val = ClientUtilDate.parseDate(ClientUtilConvert.toStr(val));
                        } else if (val instanceof Double) {
                            val = DateUtil.getJavaDate((Double) val);
                        }
                    }
                    if (fieldType != null) {
                        ExcelExport attr = field.getAnnotation(ExcelExport.class);
                        String propertyName = field.getName();
                        if (StringUtils.isNotEmpty(attr.targetAttr())) {
                            propertyName = field.getName() + "." + attr.targetAttr();
                        } else if (StringUtils.isNotEmpty(attr.readConverterExp())) {
                            val = reverseByExp(String.valueOf(val), attr.readConverterExp());
                        }
                        ReflectUtil.invokeSetter(entity, propertyName, val);
                    }
                }
                list.add(entity);
            }
        }
        return list;
    }

    private boolean isRowEmpty(Row row) {
        if (row == null) {
            return true;
        }
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && StringUtils.isNotBlank(cell.toString())) {
                return false;
            }
        }
        return true;
    }

    public List<Row> importExcelFixed(String sheetName, InputStream is) throws Exception {
        this.wb = WorkbookFactory.create(is);
        List<Row> rowList = new ArrayList<>();
        Sheet sheet;
        if (StringUtils.isNotEmpty(sheetName)) {
            sheet = wb.getSheet(sheetName);
        } else {
            sheet = wb.getSheetAt(0);
        }
        if (sheet == null) {
            throw new IOException("文件sheet不存在");
        }
        int rows = sheet.getPhysicalNumberOfRows();
        if (rows > 0) {
            for (int i = 1; i < rows; i++) {
                Row row = sheet.getRow(i);
                rowList.add(row);
            }
        }
        return rowList;
    }

    public ClientAjaxResult exportExcel(List<T> list, String sheetName) {
        this.init(list, sheetName, ExcelExport.Type.EXPORT);
        return exportExcel();
    }

    public byte[] exportExcelBytes(List<T> list, String sheetName) {
        this.init(list, sheetName, ExcelExport.Type.EXPORT);
        return exportExcelBytes();
    }

    public ClientAjaxResult downloadExcelTemplate(String sheetName) {
        this.init(null, sheetName, ExcelExport.Type.IMPORT);
        return exportExcel();
    }

    public ClientAjaxResult exportExcel() {
        OutputStream out = null;
        try {
            double sheetNo = Math.ceil(list.size() / SHEET_SIZE);
            for (int index = 0; index <= sheetNo; index++) {
                createSheet(sheetNo, index);
                Row row = sheet.createRow(0);
                int column = 0;
                for (Object[] os : fields) {
                    ExcelExport excelExport = (ExcelExport) os[1];
                    this.createCell(excelExport, row, column++);
                }
                if (ExcelExport.Type.EXPORT.equals(type)) {
                    fillExcelData(index, row);
                }
            }
            String filename = encodingFilename(sheetName);
            out = new FileOutputStream(getAbsoluteFile(filename));
            wb.write(out);
            filename = clientCore.tool.getGlobalConfigVal("SEAS_SYS_CONFIG:seas.file.disk.path.tm") + "/" + filename;
            return ClientAjaxResult.success(filename);
        } catch (Exception e) {
            log.error("导出Excel异常", e);
            throw new ClientCustomException("导出Excel失败，请联系管理员！");
        } finally {
            closeResources(out);
        }
    }

    public byte[] exportExcelBytes() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            double sheetNo = Math.ceil(list.size() / SHEET_SIZE);
            for (int index = 0; index <= sheetNo; index++) {
                createSheet(sheetNo, index);
                Row row = sheet.createRow(0);
                int column = 0;
                for (Object[] os : fields) {
                    ExcelExport excelExport = (ExcelExport) os[1];
                    this.createCell(excelExport, row, column++);
                }
                if (ExcelExport.Type.EXPORT.equals(type)) {
                    fillExcelData(index, row);
                }
            }
            wb.write(out);
            byte[] bytes = out.toByteArray();
            out.close();
            return bytes;
        } catch (Exception e) {
            log.error("导出Excel异常", e);
            throw new ClientCustomException("导出Excel失败，请联系管理员！");
        } finally {
            closeResources(out);
        }
    }

    private void closeResources(OutputStream out) {
        if (wb != null) {
            try {
                wb.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void fillExcelData(int index, Row row) {
        int startNo = index * SHEET_SIZE;
        int endNo = Math.min(startNo + SHEET_SIZE, list.size());
        for (int i = startNo; i < endNo; i++) {
            row = sheet.createRow(i + 1 - startNo);
            T vo = (T) list.get(i);
            int column = 0;
            for (Object[] os : fields) {
                Field field = (Field) os[0];
                ExcelExport excelExport = (ExcelExport) os[1];
                field.setAccessible(true);
                this.addCell(excelExport, row, vo, field, column++);
            }
        }
    }

    private Map<String, CellStyle> createStyles(Workbook wb) {
        Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        Font dataFont = wb.createFont();
        dataFont.setFontName("Arial");
        dataFont.setFontHeightInPoints((short) 10);
        style.setFont(dataFont);
        styles.put("data", style);

        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font headerFont = wb.createFont();
        headerFont.setFontName("Arial");
        headerFont.setFontHeightInPoints((short) 10);
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(headerFont);
        styles.put("header", style);
        return styles;
    }

    public Cell createCell(ExcelExport attr, Row row, int column) {
        Cell cell = row.createCell(column);
        cell.setCellValue(attr.name());
        setDataValidation(attr, row, column);
        cell.setCellStyle(styles.get("header"));
        return cell;
    }

    public void setCellVo(Object value, ExcelExport attr, Cell cell) {
        if (ExcelExport.ColumnType.STRING == attr.cellType()) {
            cell.setCellType(CellType.NUMERIC);
            cell.setCellValue(value == null ? attr.defaultValue() : value + attr.suffix());
        } else if (ExcelExport.ColumnType.NUMERIC == attr.cellType()) {
            cell.setCellType(CellType.NUMERIC);
            cell.setCellValue(Integer.parseInt(value + ""));
        }
    }

    public void setDataValidation(ExcelExport attr, Row row, int column) {
        if (attr.name().indexOf("注：") >= 0) {
            sheet.setColumnWidth(column, 6000);
        } else {
            sheet.setColumnWidth(column, (int) ((attr.width() + 0.72) * 256));
            row.setHeight((short) (attr.height() * 20));
        }
        if (StringUtils.isNotEmpty(attr.prompt())) {
            setXSSFPrompt(sheet, "", attr.prompt(), 1, 100, column, column);
        }
        if (attr.combo().length > 0) {
            setXSSFValidation(sheet, attr.combo(), 1, 100, column, column);
        }
    }

    public Cell addCell(ExcelExport attr, Row row, T vo, Field field, int column) {
        Cell cell = null;
        try {
            row.setHeight((short) (attr.height() * 20));
            if (attr.isExport()) {
                cell = row.createCell(column);
                cell.setCellStyle(styles.get("data"));
                Object value = getTargetValue(vo, field, attr);
                String dateFormat = attr.dateFormat();
                String readConverterExp = attr.readConverterExp();
                if (StringUtils.isNotEmpty(dateFormat) && value != null) {
                    cell.setCellValue(ClientUtilDate.parseDateToStr(dateFormat, (Date) value));
                } else if (StringUtils.isNotEmpty(readConverterExp) && value != null) {
                    cell.setCellValue(convertByExp(String.valueOf(value), readConverterExp));
                } else {
                    setCellVo(value, attr, cell);
                }
            }
        } catch (Exception e) {
            log.error("导出Excel失败{}", e);
        }
        return cell;
    }

    public void setXSSFPrompt(Sheet sheet, String promptTitle, String promptContent, int firstRow, int endRow,
                              int firstCol, int endCol) {
        DataValidationHelper helper = sheet.getDataValidationHelper();
        DataValidationConstraint constraint = helper.createCustomConstraint("DD1");
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        DataValidation dataValidation = helper.createValidation(constraint, regions);
        dataValidation.createPromptBox(promptTitle, promptContent);
        dataValidation.setShowPromptBox(true);
        sheet.addValidationData(dataValidation);
    }

    public void setXSSFValidation(Sheet sheet, String[] textlist, int firstRow, int endRow, int firstCol, int endCol) {
        DataValidationHelper helper = sheet.getDataValidationHelper();
        DataValidationConstraint constraint = helper.createExplicitListConstraint(textlist);
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        DataValidation dataValidation = helper.createValidation(constraint, regions);
        if (dataValidation instanceof XSSFDataValidation) {
            dataValidation.setSuppressDropDownArrow(true);
            dataValidation.setShowErrorBox(true);
        } else {
            dataValidation.setSuppressDropDownArrow(false);
        }
        sheet.addValidationData(dataValidation);
    }

    public static String convertByExp(String propertyValue, String converterExp) throws Exception {
        try {
            String[] convertSource = converterExp.split(",");
            for (String item : convertSource) {
                String[] itemArray = item.split("=");
                if (itemArray[0].equals(propertyValue)) {
                    return itemArray[1];
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return propertyValue;
    }

    public static String reverseByExp(String propertyValue, String converterExp) throws Exception {
        try {
            String[] convertSource = converterExp.split(",");
            for (String item : convertSource) {
                String[] itemArray = item.split("=");
                if (itemArray[1].equals(propertyValue)) {
                    return itemArray[0];
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return propertyValue;
    }

    public String encodingFilename(String filename) {
        return filename + ".xlsx";
    }

    public String getAbsoluteFile(String filename) {
        String downloadPath = clientCore.tool.getGlobalConfigVal("SEAS_SYS_CONFIG:seas.file.disk.path.tm") + "/" + filename;
        File desc = new File(downloadPath);
        if (!desc.getParentFile().exists()) {
            desc.getParentFile().mkdirs();
        }
        return downloadPath;
    }

    private Object getTargetValue(T vo, Field field, ExcelExport excelExport) throws Exception {
        Object o = field.get(vo);
        if (StringUtils.isNotEmpty(excelExport.targetAttr())) {
            String target = excelExport.targetAttr();
            if (target.indexOf(".") > -1) {
                String[] targets = target.split("[.]");
                for (String name : targets) {
                    o = getValue(o, name);
                }
            } else {
                o = getValue(o, target);
            }
        }
        return o;
    }

    private Object getValue(Object o, String name) throws Exception {
        if (StringUtils.isNotEmpty(name)) {
            Class<?> clazz = o.getClass();
            String methodName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
            Method method = clazz.getMethod(methodName);
            o = method.invoke(o);
        }
        return o;
    }

    private void createExcelField() {
        this.fields = new ArrayList<Object[]>();
        List<Field> tempFields = new ArrayList<>();
        tempFields.addAll(Arrays.asList(clazz.getSuperclass().getDeclaredFields()));
        tempFields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        for (Field field : tempFields) {
            if (field.isAnnotationPresent(ExcelExport.class)) {
                putToField(field, field.getAnnotation(ExcelExport.class));
            }
        }
    }

    private void putToField(Field field, ExcelExport attr) {
        if (attr != null && (attr.type() == ExcelExport.Type.ALL || attr.type() == type)) {
            this.fields.add(new Object[]{field, attr});
        }
    }

    public void createWorkbook() {
        this.wb = new SXSSFWorkbook(500);
    }

    public void createSheet(double sheetNo, int index) {
        this.sheet = wb.createSheet();
        this.styles = createStyles(wb);
        if (sheetNo == 0) {
            wb.setSheetName(index, sheetName);
        } else {
            wb.setSheetName(index, sheetName + index);
        }
    }

    public Object getCellValue(Row row, int column) {
        if (row == null) {
            return row;
        }
        Object val = "";
        try {
            Cell cell = row.getCell(column);
            if (cell != null) {
                if (cell.getCellType() == CellType.NUMERIC || cell.getCellType() == CellType.FORMULA) {
                    val = cell.getNumericCellValue();
                    if (DateUtil.isCellDateFormatted(cell)) {
                        val = DateUtil.getJavaDate((Double) val);
                    } else {
                        if ((Double) val % 1 > 0) {
                            val = new DecimalFormat("0.00").format(val);
                        } else {
                            val = new DecimalFormat("0").format(val);
                        }
                    }
                } else if (cell.getCellType() == CellType.STRING) {
                    val = cell.getStringCellValue();
                } else if (cell.getCellType() == CellType.BOOLEAN) {
                    val = cell.getBooleanCellValue();
                } else if (cell.getCellType() == CellType.ERROR) {
                    val = cell.getErrorCellValue();
                }
            }
        } catch (Exception e) {
            return val;
        }
        return val;
    }
}
