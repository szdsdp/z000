package com.szd.z000.web.demo.expCost.service;

import com.alibaba.fastjson.JSONObject;
import com.szd.core.client.annotation.ClientProgApi;
import com.szd.core.client.annotation.ClientProgApiInf;
import com.szd.core.client.domain.common.ClientApiEntity;
import com.szd.core.client.domain.common.ClientApiHead;
import com.szd.core.client.domain.common.ClientResultMsgBox;
import com.szd.core.client.domain.wf.*;
import com.szd.z000.web.demo.expCost.domain.ExpCostDataVO;
import com.szd.z000.web.demo.expCost.domain.ExpCostParamVO;
import io.netty.util.internal.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 批量审批-DEMO报账
 */
@Service
@ClientProgApi(proCode = "Z000_DEMO_EXP_01A_WF_APPR", proName = "批量审批-DEMO报账")
public class ExpCostBatchApprService implements ClientProgApiInf {
    @Resource
    private ExpCostService expCostService;;

    @Override
    public ClientApiEntity process(ClientApiEntity clientApiEntity) {
        ClientApiHead head = clientApiEntity.getHead();
        JSONObject param = clientApiEntity.getBody();
        ClientWfApprRet ret = new ClientWfApprRet();
        try {
            try {
                ClientWfApprParam clientWfApprParam = JSONObject.toJavaObject(param, ClientWfApprParam.class);
                ClientWfApprBuss buss = clientWfApprParam.getBuss();
                ClientWfParamEvt wfEvt = clientWfApprParam.getWfEvt();
                // 检查参数
                checkWfAppr(buss, wfEvt);
                // 审批
                ExpCostDataVO dataVO = expCostService.selectData(buss.getBussId());
                if (ObjectUtils.isEmpty(dataVO) || ObjectUtils.isEmpty(dataVO.getHeader())) {
                    throw new Exception("业务单据不存在");
                }
                ExpCostParamVO paramVO = new ExpCostParamVO();
                paramVO.setData(dataVO);
                paramVO.setWfEvt(wfEvt);
                ClientWfReturn returnVO = expCostService.saveData(paramVO);
                // 返回结果
                ret.setBussId(buss.getBussId());
                if (!CollectionUtils.isEmpty(returnVO.getRetMsgBox()) && returnVO.getRetMsgBox().stream().anyMatch(msg -> "1".equals(msg.getLight()))) {
                    ret.setRetCode("E");
                    List<ClientResultMsgBox> msgBoxDataList = returnVO.getRetMsgBox().stream().filter(msg -> "1".equals(msg.getLight())).toList();
                    StringBuilder msgSb = new StringBuilder();
                    msgBoxDataList.forEach(msgBoxData -> {
                        msgSb.append(msgBoxData.getRetCode()).append(":").append(msgBoxData.getRetMsg()).append(";");
                    });
                    ret.setRetMsg(msgSb.toString());
                } else {
                    ret.setRetMsg("审批成功");
                    ret.setRetCode("S");
                }
            } catch (Exception ex) {
                ret.setRetCode("E");
                ret.setRetMsg(ex.getMessage());
            }
        } catch (Exception e) {
            ret.setRetCode("E");
            ret.setRetMsg(StringUtil.isNullOrEmpty(e.getMessage()) ? "系统错误,请联系技术人员" : e.getMessage());
        }
        return new ClientApiEntity(head, (JSONObject) JSONObject.toJSON(ret));
    }

    public void checkWfAppr(ClientWfApprBuss buss, ClientWfParamEvt wfEvt) throws Exception {
        if (buss == null || StringUtil.isNullOrEmpty(buss.getRouterCode())) {
            throw new Exception("路由编码routeCode不能为空");
        }
        if (StringUtil.isNullOrEmpty(buss.getBussId())) {
            throw new Exception("业务单号bussId不能为空");
        }
        if (wfEvt == null) {
            throw new Exception("流程参数wfEvt不能为空");
        }
    }
}
