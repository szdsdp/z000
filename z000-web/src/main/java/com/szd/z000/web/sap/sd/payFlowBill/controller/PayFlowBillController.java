package com.szd.z000.web.sap.sd.payFlowBill.controller;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.*;
import com.szd.core.client.domain.file.ClientFileUploadObj;
import com.szd.core.client.domain.file.ClientFileUploadParam;
import com.szd.core.client.service.ClientBaseController;
import com.szd.z000.base.common.excel.ExcelUtil;
import com.szd.z000.base.common.util.BytesToMultipartFile;
import com.szd.z000.base.orm.domain.sd.payFlowBill.PayFlowBill;
import com.szd.z000.web.sap.sd.payFlowBill.domain.UpdateCustParam;
import com.szd.z000.web.sap.sd.payFlowBill.service.PayFlowBillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Base64;
import java.util.List;

/**
 * 票据承兑
 */
@Slf4j
@RestController
@RequestMapping("/z000/sd/payFlowBill")
public class PayFlowBillController extends ClientBaseController {
    @Resource
    private PayFlowBillService payFlowBillService;
    @Resource
    private ClientCore clientCore;

    /**
     * 查询列表
     */
    @PostMapping("/list")
    public ClientPageTableRet list(@RequestBody ClientRpParam param) {
        startPage(param.getPageNum(), param.getPageSize());
        List<PayFlowBill> list = payFlowBillService.selectList(param);
        // 设置描述字段
        getDescList(list);
        return getPageTable(list);
    }

    /**
     * 查询详情
     */
    @GetMapping("/get")
    public ClientAjaxResult get(@RequestParam("sysBillNo") String sysBillNo) {
        PayFlowBill result = payFlowBillService.selectById(sysBillNo);
        // 设置描述字段
        getDesc(result);
        return ClientAjaxResult.success(result);
    }

    /**
     * 新增
     */
    @PostMapping("/save")
    public ClientAjaxResult save(@RequestBody PayFlowBill bill) {
        payFlowBillService.save(bill);
        return ClientAjaxResult.success();
    }

    /**
     * 修改
     */
    @PostMapping("/edit")
    public ClientAjaxResult edit(@RequestBody PayFlowBill bill) {
        payFlowBillService.edit(bill);
        return ClientAjaxResult.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public ClientAjaxResult delete(@RequestParam("sysBillNo") String sysBillNo) {
        payFlowBillService.delete(sysBillNo);
        return ClientAjaxResult.success();
    }

    /**
     * 更新客户
     */
    @PostMapping("/updateCust")
    public ClientAjaxResult updateCust(@RequestBody UpdateCustParam updateCustParam) {
        ClientResult clientResult = payFlowBillService.updateCust(updateCustParam);
        return ClientAjaxResult.success(clientResult);
    }

    /**
     * 导入Excel
     */
    @PostMapping("/import")
    public ClientAjaxResult importData(@RequestBody ClientFileUploadParam fileUploadParam) {
        if (CollectionUtils.isEmpty(fileUploadParam.getFileUpload())) {
            return ClientAjaxResult.error("未上传文件");
        }
        for (ClientFileUploadObj fileUploadObj : fileUploadParam.getFileUpload()) {
            try {
                // 解码Base64数据
                String[] baseStrs = fileUploadObj.getFileBase64().split(",");
                byte[] decodedBytes = Base64.getDecoder().decode(baseStrs[1]);
                // 处理字节符号位
                for (int i = 0; i < decodedBytes.length; ++i) {
                    if (decodedBytes[i] < 0) {
                        decodedBytes[i] = (byte) (decodedBytes[i] + 256);
                    }
                }
                // 转换为MultipartFile
                MultipartFile file = new BytesToMultipartFile(decodedBytes, fileUploadObj.getFileName(),
                        fileUploadObj.getFileName(), baseStrs[0], decodedBytes.length);
                // 解析Excel文件
                ExcelUtil<PayFlowBill> excelUtil = new ExcelUtil<>(PayFlowBill.class);
                List<PayFlowBill> dataList = excelUtil.importExcel("收票票据", file.getInputStream());
                if (CollectionUtils.isEmpty(dataList)) {
                    continue;
                }
                // 执行导入
                List<ClientResultMsgBox> msgList = payFlowBillService.importData(dataList);
                return ClientAjaxResult.success(msgList);
            } catch (Exception e) {
                log.error("导入文件失败: {}", fileUploadObj.getFileName(), e);
                return ClientAjaxResult.error("导入失败: " + e.getMessage());
            }
        }
        return ClientAjaxResult.success("未找到有效数据");
    }

    /**
     * 设置列表描述字段（公司、票据类型、票据范围、客户）
     */
    private void getDescList(List<PayFlowBill> bills) {
        if (CollectionUtils.isEmpty(bills)) {
            return;
        }
        for (PayFlowBill bill : bills) {
            bill.setCmpyName(clientCore.mdm.getDescA("CORE_CMPY", bill.getCmpy(), "0"));
            bill.setBillTypeName(clientCore.mdm.getDescA("Z000_BILL_TYPE", bill.getBillType(), "0"));
            bill.setBillScopeName(clientCore.mdm.getDescA("Z000_BILL_RANGE", bill.getBillScope(), "0"));
            bill.setCustName(clientCore.mdm.getDescA("CORE_CUST", bill.getCust(), "0"));
        }
    }

    /**
     * 设置详情描述字段
     */
    private void getDesc(PayFlowBill result) {
        if (result != null) {
            result.setCmpyName(clientCore.mdm.getDescA("CORE_CMPY", result.getCmpy(), "1"));
            result.setCustName(clientCore.mdm.getDescA("CORE_CUST", result.getCust(), "1"));
        }
    }
}
