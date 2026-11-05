package com.szd.z000.web.sap.mdm.bankAcc.controller;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientAjaxResult;
import com.szd.core.client.domain.common.ClientPageTableRet;
import com.szd.core.client.domain.common.ClientResultMsgBox;
import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.core.client.domain.file.ClientFileUploadObj;
import com.szd.core.client.domain.file.ClientFileUploadParam;
import com.szd.core.client.service.ClientBaseController;
import com.szd.z000.base.common.excel.ExcelUtil;
import com.szd.z000.base.common.util.BytesToMultipartFile;
import com.szd.z000.base.orm.domain.mdm.bankAcc.BankAcc;
import com.szd.z000.web.sap.mdm.bankAcc.service.BankAccService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Base64;
import java.util.List;

/**
 * 本方账户
 */
@Slf4j
@RestController
@RequestMapping("/z000/mdm/bankAcc")
public class BankAccController extends ClientBaseController {
    @Resource
    private BankAccService bankAccService;
    @Resource
    private ClientCore clientCore;

    /**
     * 查询列表
     */
    @PostMapping("/list")
    public ClientPageTableRet list(@RequestBody ClientRpParam param) {
        startPage(param.getPageNum(), param.getPageSize());
        List<BankAcc> result = bankAccService.selectList(param);
        // 设置描述字段
        getDescList(result);
        return getPageTable(result);
    }

    /**
     * 查询详情
     */
    @GetMapping("/get")
    public ClientAjaxResult get(@RequestParam("accNo") String accNo) {
        BankAcc result = bankAccService.selectById(accNo);
        // 设置描述字段
        getDesc(result);
        return ClientAjaxResult.success(result);
    }

    /**
     * 新增
     */
    @PostMapping("/save")
    public ClientAjaxResult save(@RequestBody BankAcc bankAcc) {
        bankAccService.save(bankAcc);
        return ClientAjaxResult.success();
    }

    /**
     * 修改
     */
    @PostMapping("/edit")
    public ClientAjaxResult edit(@RequestBody BankAcc bankAcc) {
        bankAccService.edit(bankAcc);
        return ClientAjaxResult.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public ClientAjaxResult delete(@RequestParam("accNo") String accNo) {
        bankAccService.delete(accNo);
        return ClientAjaxResult.success();
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
                ExcelUtil<BankAcc> excelUtil = new ExcelUtil<>(BankAcc.class);
                List<BankAcc> dataList = excelUtil.importExcel("本方账户", file.getInputStream());
                if (CollectionUtils.isEmpty(dataList)) {
                    continue;
                }
                // 执行导入
                List<ClientResultMsgBox> msgList = bankAccService.importData(dataList);
                return ClientAjaxResult.success(msgList);
            } catch (Exception e) {
                log.error("导入文件失败: {}", fileUploadObj.getFileName(), e);
                return ClientAjaxResult.error("导入失败: " + e.getMessage());
            }
        }
        return ClientAjaxResult.success("未找到有效数据");
    }

    /**
     * 设置列表描述字段（公司、科目、币种）
     */
    private void getDescList(List<BankAcc> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        for (BankAcc bankAcc : list) {
            bankAcc.setCmpyName(clientCore.mdm.getDescA("CORE_CMPY", bankAcc.getCmpy(), "0"));
            bankAcc.setAccsName(clientCore.mdm.getDescB("CORE_ACCS", "NLYY", bankAcc.getAccs(), "0"));
            bankAcc.setCurrName(clientCore.mdm.getDescA("CORE_CURR", bankAcc.getCurr(), "0"));
        }
    }

    /**
     * 设置详情描述字段
     */
    private void getDesc(BankAcc bankAcc) {
        if (bankAcc == null) {
            return;
        }
        bankAcc.setCmpyName(clientCore.mdm.getDescA("CORE_CMPY", bankAcc.getCmpy(), "1"));
        bankAcc.setAccsName(clientCore.mdm.getDescB("CORE_ACCS", "NLYY", bankAcc.getAccs(), "1"));
    }
}
