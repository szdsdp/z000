package com.szd.z000.web.sap.sd.payRece.controller;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientAjaxResult;
import com.szd.core.client.domain.common.ClientPageTableRet;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.core.client.domain.wf.ClientWfOper;
import com.szd.core.client.domain.wf.ClientWfReturn;
import com.szd.core.client.exception.ClientCustomException;
import com.szd.core.client.service.ClientBaseController;
import com.szd.z000.base.orm.domain.sd.payRece.PayReceiptHeader;
import com.szd.z000.base.orm.domain.sd.payRece.PayReceiptItem;
import com.szd.z000.web.sap.sd.payRece.domain.*;
import com.szd.z000.web.sap.sd.payRece.service.PayReceiptSapService;
import com.szd.z000.web.sap.sd.payRece.service.PayReceiptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 回款认领接口
 */
@Slf4j
@RestController
@RequestMapping("/z000/sd/payRece")
public class PayReceiptController extends ClientBaseController {
    @Resource
    private PayReceiptService payReceiptService;
    @Resource
    private PayReceiptSapService payReceiptSapService;
    @Resource
    private ClientCore clientCore;

    /**
     * 回款认领清单
     */
    @PostMapping("/list")
    public ClientPageTableRet list(@RequestBody ClientRpParam param) {
        startPage(param.getPageNum(), param.getPageSize());
        List<PayReceiptHeader> list = payReceiptService.selectList(param);
        // 设置描述字段
        getDescList(list);
        return getPageTable(list);
    }

    /**
     * 生成回款认领单（票据/电汇快捷入口）
     */
    @PostMapping("/createPayReceiptDoc")
    public ClientAjaxResult createPayReceiptDoc(@RequestBody PayReceApplyParam payReceApplyParam) {
        try {
            CommonApplyResult result = payReceiptService.createPayReceiptDoc(payReceApplyParam);
            return ClientAjaxResult.success(result);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    /**
     * 获取认领初始数据
     */
    @GetMapping("/getDataInit/{bankDocType}/{bankDoc}")
    public ClientAjaxResult getDataInit(@PathVariable("bankDocType") String bankDocType, @PathVariable("bankDoc") String bankDoc) {
        try {
            PayReceResultVO resultVO = payReceiptService.getDataInit(bankDocType, bankDoc);
            return ClientAjaxResult.success(resultVO);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    /**
     * 页面查询（含业务数据、流程按钮、字段状态）
     */
    @GetMapping("/get")
    public ClientAjaxResult getData(String bussId, String userId, String option) {
        // 业务数据
        PayReceDataVO data = payReceiptService.selectData(bussId);
        // 添加描述
        getDesc(data);
        // 流程操作授权
        ClientWfOper wfOper = clientCore.wf.wfInit(bussId, userId, option);
        // 返回
        PayReceResultVO resultVO = new PayReceResultVO();
        resultVO.setData(data);
        resultVO.setWfOper(wfOper);
        return ClientAjaxResult.success(resultVO);
    }

    /**
     * 数据检查
     */
    @PostMapping("/check")
    public ClientAjaxResult check(@RequestBody PayReceParamVO payReceParamVO) {
        try {
            ClientResult checkResult = payReceiptService.check(payReceParamVO);
            return ClientAjaxResult.success(checkResult);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    /**
     * 页面按钮操作（保存草稿、提交、同意、驳回等）
     */
    @PostMapping("/save")
    public ClientAjaxResult saveData(@RequestBody PayReceParamVO payReceParamVO) {
        try {
            ClientWfReturn clientWfReturn = payReceiptService.saveData(payReceParamVO);
            return ClientAjaxResult.success(clientWfReturn);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    /**
     * 同步SAP回款认领单
     */
    @GetMapping("/genSapDoc/{bussId}")
    public ClientAjaxResult genSapDoc(@PathVariable("bussId") String bussId) {
        try {
            ClientResult clientResult = payReceiptSapService.createSapPayReceipt(bussId);
            return ClientAjaxResult.success(clientResult);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    /**
     * 冲销SAP回款认领单
     */
    @GetMapping("/reverseSapDoc/{bussId}")
    public ClientAjaxResult reverseSapDoc(@PathVariable("bussId") String bussId) {
        try {
            ClientResult clientResult = payReceiptSapService.reverseSapPayReceipt(bussId);
            return ClientAjaxResult.success(clientResult);
        } catch (ClientCustomException e) {
            return ClientAjaxResult.error(e.getMessage());
        }
    }

    /**
     * 设置列表描述字段
     */
    private void getDescList(List<PayReceiptHeader> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        for (PayReceiptHeader header : list) {
            header.setBussId(header.getReceiptId());
            header.setUserName(clientCore.mdm.getDescA("CORE_USER", header.getUserId(), "0"));
            header.setCreateByName(clientCore.mdm.getDescA("CORE_USER", header.getCreateBy(), "0"));
            header.setUpdateByName(clientCore.mdm.getDescA("CORE_USER", header.getUpdateBy(), "0"));
            header.setBussStatusName(clientCore.mdm.getDescA("Z000_DOC_STAU", header.getBussStatus(), "0"));
            header.setBstpName(clientCore.mdm.getDescA("CORE_BSTP", header.getBstp(), "0"));
            header.setPayNatureName(clientCore.mdm.getDescA("Z000_PAY_NATURE", header.getPayNature(), "0"));
            header.setPayWayName(clientCore.mdm.getDescA("Z000_PAY_WAY", header.getPayWay(), "0"));
            header.setPrfcName(clientCore.mdm.getDescA("CORE_PRFC", header.getPrfc(), "0"));
            header.setCmpyName(clientCore.mdm.getDescA("CORE_CMPY", header.getCmpy(), "0"));
            header.setCustName(clientCore.mdm.getDescA("CORE_CUST", header.getCust(), "0"));
            header.setRegionName(clientCore.mdm.getDescA("Z000_MDM_REGION", header.getRegion(), "0"));
            header.setSalesManagerName(clientCore.mdm.getDescA("CORE_USER", header.getSalesManager(), "0"));
        }
    }

    /**
     * 设置详情描述字段
     */
    private void getDesc(PayReceDataVO data) {
        PayReceiptHeader header = data.getHeader();
        if (header != null) {
            header.setCmpyName(clientCore.mdm.getDescA("CORE_CMPY", header.getCmpy(), "1"));
            header.setCstcName(clientCore.mdm.getDescA("CORE_CSTC", header.getCstc(), "1"));
            header.setUserName(clientCore.mdm.getDescA("CORE_USER", header.getUserId(), "1"));
            header.setBstpName(clientCore.mdm.getDescA("CORE_BSTP", header.getBstp(), "1"));
            header.setPrfcName(clientCore.mdm.getDescA("CORE_PRFC", header.getPrfc(), "1"));
            header.setCustName(clientCore.mdm.getDescA("CORE_CUST", header.getCust(), "1"));
        }
        // 行项目描述
        List<PayReceiptItem> itemList = data.getItemList();
        if (!CollectionUtils.isEmpty(itemList)) {
            for (PayReceiptItem item : itemList) {
                item.setAccsName(clientCore.mdm.getDescB("CORE_ACCS", "NLYY", item.getAccs(), "1"));
            }
        }
    }
}
