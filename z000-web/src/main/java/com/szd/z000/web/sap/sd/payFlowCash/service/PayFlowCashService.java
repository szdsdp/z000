package com.szd.z000.web.sap.sd.payFlowCash.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.domain.common.ClientResultMsgBox;
import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.core.client.exception.ClientCustomException;
import com.szd.core.client.util.ClientUtilDate;
import com.szd.z000.base.orm.domain.sd.payFlowCash.PayFlowCash;
import com.szd.z000.base.orm.mapper.sd.payFlowCash.PayFlowCashMapper;
import com.szd.z000.web.sap.sd.payFlowBill.domain.UpdateCustParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.util.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 电汇回款服务
 */
@Service
@Slf4j
public class PayFlowCashService {
    @Resource
    private PayFlowCashMapper payFlowCashMapper;
    @Resource
    private ClientCore clientCore;

    /**
     * 查询列表
     */
    public List<PayFlowCash> selectList(ClientRpParam param) {
        return payFlowCashMapper.selectList(param);
    }

    /**
     * 查询详情
     */
    public PayFlowCash selectById(String bankDoc) {
        return payFlowCashMapper.selectById(bankDoc);
    }

    /**
     * 新增电汇回款
     */
    public void save(PayFlowCash entity) {
        // 校验数据唯一性
        checkDataAdd(entity);
        // 生成银行回单号
        ClientResult numDoc = clientCore.tool.getNumDocB("Z000_PAY_CASH", "");
        entity.setBankDoc(numDoc.getDocId());
        try {
            payFlowCashMapper.insert(entity);
        } catch (Exception e) {
            throw new ClientCustomException("保存失败");
        }
    }

    /**
     * 修改电汇回款
     */
    public void edit(PayFlowCash entity) {
        // 校验数据唯一性
        checkData(entity);
        try {
            payFlowCashMapper.update(entity);
        } catch (Exception e) {
            throw new ClientCustomException("更新失败");
        }
    }

    /**
     * 删除电汇回款
     */
    public void delete(String bankDoc) {
        payFlowCashMapper.deleteById(bankDoc);
    }

    /**
     * 更新客户
     */
    public ClientResult updateCust(UpdateCustParam updateCustParam) {
        ClientResult clientResult = new ClientResult();
        payFlowCashMapper.updateCust(updateCustParam.getSysId(), updateCustParam.getCust());
        clientResult.setStatus("S");
        clientResult.setMsg("更新客户成功");
        return clientResult;
    }

    /**
     * 导入数据（新增+更新）
     */
    public List<ClientResultMsgBox> importData(List<PayFlowCash> list) {
        List<ClientResultMsgBox> retBoxList = new ArrayList<>();
        List<PayFlowCash> insertList = new ArrayList<>();
        List<PayFlowCash> updateList = new ArrayList<>();
        try {
            // 逐行校验数据
            for (int i = 0; i < list.size(); i++) {
                PayFlowCash newObj = list.get(i);
                // 转换日期格式（YYYYMMDD → Date）
                newObj.setTranDate(ClientUtilDate.sapToDate(newObj.getTranDateStr()));
                checkImport(insertList, updateList, retBoxList, newObj, i);
            }
            // 存在校验错误则直接返回
            if (!CollectionUtils.isEmpty(retBoxList)) {
                return retBoxList;
            }
            // 批量新增
            if (CollectionUtils.isNotEmpty(insertList)) {
                insertList.forEach(i -> {
                    ClientResult numDoc = clientCore.tool.getNumDocB("Z000_PAY_CASH", "");
                    i.setBankDoc(numDoc.getDocId());
                });
                payFlowCashMapper.batchInsert(insertList);
                retBoxList.add(new ClientResultMsgBox("3", "", "S", "导入成功，新增" + insertList.size() + "条数据"));
            }
            // 批量更新
            if (CollectionUtils.isNotEmpty(updateList)) {
                payFlowCashMapper.batchUpdate(updateList);
                retBoxList.add(new ClientResultMsgBox("3", "", "S", "导入成功，更新" + updateList.size() + "条数据"));
            }
        } catch (Exception e) {
            log.error("导入电汇回款数据失败", e);
            throw new ClientCustomException("数据导入失败: " + e.getMessage(), e);
        }
        return retBoxList;
    }

    /**
     * 校验导入数据，区分新增和更新
     */
    private void checkImport(List<PayFlowCash> insertList, List<PayFlowCash> updateList,
                             List<ClientResultMsgBox> retBoxList, PayFlowCash newObj, int index) {
        int rowNum = index + 1;
        // 必填字段校验
        if (StringUtil.isBlank(newObj.getBankFlowNo())) {
            retBoxList.add(new ClientResultMsgBox("1", "", "E", "第" + rowNum + "行，银行流水号不能为空"));
        }
        if (StringUtil.isBlank(newObj.getMyAccNo())) {
            retBoxList.add(new ClientResultMsgBox("1", "", "E", "第" + rowNum + "行，本方账户不能为空"));
        }
        if (newObj.getAmt() == null) {
            retBoxList.add(new ClientResultMsgBox("1", "", "E", "第" + rowNum + "行，金额不能为空"));
        }
        if (newObj.getTranDate() == null) {
            retBoxList.add(new ClientResultMsgBox("1", "", "E", "第" + rowNum + "行，交易日期不能为空"));
        }
        // 存在校验错误则跳过该行
        if (!CollectionUtils.isEmpty(retBoxList)) {
            return;
        }
        // 根据银行流水号+本方账户判断是新增还是更新
        PayFlowCash oldObj = payFlowCashMapper.selectByParams(newObj.getBankFlowNo(), newObj.getMyAccNo());
        if (oldObj != null) {
            updateList.add(newObj);
        } else {
            insertList.add(newObj);
        }
    }

    /**
     * 校验修改数据有效性
     */
    public void checkData(PayFlowCash entity) {
        clientCore.mdm.checkAE("CORE_CMPY", entity.getCmpy(), "", "0");
    }

    /**
     * 校验新增数据唯一性
     */
    public void checkDataAdd(PayFlowCash entity) {
        PayFlowCash exists = payFlowCashMapper.selectByParams(entity.getBankFlowNo(), entity.getMyAccNo());
        if (exists != null) {
            throw new ClientCustomException("银行流水号、本方账户已存在");
        }
        checkData(entity);
    }
}
