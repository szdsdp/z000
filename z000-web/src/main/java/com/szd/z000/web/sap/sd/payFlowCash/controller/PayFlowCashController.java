package com.szd.z000.web.sap.sd.payFlowCash.controller;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientAjaxResult;
import com.szd.core.client.domain.common.ClientPageTableRet;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.domain.common.ClientResultMsgBox;
import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.core.client.domain.file.ClientFileUploadObj;
import com.szd.core.client.domain.file.ClientFileUploadParam;
import com.szd.core.client.service.ClientBaseController;
import com.szd.z000.base.common.excel.ExcelUtil;
import com.szd.z000.base.common.util.BytesToMultipartFile;
import com.szd.z000.base.orm.domain.sd.payFlowCash.PayFlowCash;
import com.szd.z000.web.sap.sd.payFlowBill.domain.UpdateCustParam;
import com.szd.z000.web.sap.sd.payFlowCash.service.PayFlowCashService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Base64;
import java.util.List;

/**
 * 电汇回款
 */
@Slf4j
@RestController
@RequestMapping("/z000/sd/payFlowCash")
public class PayFlowCashController extends ClientBaseController {
    @Resource
    private PayFlowCashService payFlowCashService;
    @Resource
    private ClientCore clientCore;

    /**
     * 查询列表
     */
    @PostMapping("/list")
    public ClientPageTableRet list(@RequestBody ClientRpParam param) {
        startPage(param.getPageNum(), param.getPageSize());
        List<PayFlowCash> list = payFlowCashService.selectList(param);
        // 设置描述字段
        getDescList(list);
        return getPageTable(list);
    }

    /**
     * 查询详情
     */
    @GetMapping("/get")
    public ClientAjaxResult get(@RequestParam("bankDoc") String bankDoc) {
        PayFlowCash result = payFlowCashService.selectById(bankDoc);
        // 设置描述字段
        getDesc(result);
        return ClientAjaxResult.success(result);
    }

    /**
     * 新增
     */
    @PostMapping("/save")
    public ClientAjaxResult save(@RequestBody PayFlowCash entity) {
        payFlowCashService.save(entity);
        return ClientAjaxResult.success();
    }

    /**
     * 修改
     */
    @PostMapping("/edit")
    public ClientAjaxResult edit(@RequestBody PayFlowCash entity) {
        payFlowCashService.edit(entity);
        return ClientAjaxResult.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public ClientAjaxResult delete(@RequestParam("bankDoc") String bankDoc) {
        payFlowCashService.delete(bankDoc);
        return ClientAjaxResult.success();
    }

    /**
     * 更新客户
     */
    @PostMapping("/updateCust")
    public ClientAjaxResult updateCust(@RequestBody UpdateCustParam updateCustParam) {
        ClientResult clientResult = payFlowCashService.updateCust(updateCustParam);
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
                ExcelUtil<PayFlowCash> excelUtil = new ExcelUtil<>(PayFlowCash.class);
                List<PayFlowCash> dataList = excelUtil.importExcel("回款流水", file.getInputStream());
                if (CollectionUtils.isEmpty(dataList)) {
                    continue;
                }
                // 执行导入
                List<ClientResultMsgBox> msgList = payFlowCashService.importData(dataList);
                return ClientAjaxResult.success(msgList);
            } catch (Exception e) {
                log.error("导入文件失败: {}", fileUploadObj.getFileName(), e);
                return ClientAjaxResult.error("导入失败: " + e.getMessage());
            }
        }
        return ClientAjaxResult.success("未找到有效数据");
    }

    /**
     * 设置列表描述字段（公司、客户）
     */
    private void getDescList(List<PayFlowCash> payFlows) {
        if (CollectionUtils.isEmpty(payFlows)) {
            return;
        }
        for (PayFlowCash entity : payFlows) {
            entity.setCmpyName(clientCore.mdm.getDescA("CORE_CMPY", entity.getCmpy(), "0"));
            entity.setCustName(clientCore.mdm.getDescA("CORE_CUST", entity.getCust(), "0"));
        }
    }

    /**
     * 设置详情描述字段
     */
    private void getDesc(PayFlowCash result) {
        if (result != null) {
            result.setCmpyName(clientCore.mdm.getDescA("CORE_CMPY", result.getCmpy(), "1"));
            result.setCustName(clientCore.mdm.getDescA("CORE_CUST", result.getCust(), "1"));
        }
    }
}
