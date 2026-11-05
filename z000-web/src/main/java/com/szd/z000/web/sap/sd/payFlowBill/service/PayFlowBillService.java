package com.szd.z000.web.sap.sd.payFlowBill.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.domain.common.ClientResultMsgBox;
import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.core.client.exception.ClientCustomException;
import com.szd.core.client.util.ClientUtilDate;
import com.szd.z000.base.orm.domain.sd.payFlowBill.PayFlowBill;
import com.szd.z000.base.orm.mapper.sd.payFlowBill.PayFlowBillMapper;
import com.szd.z000.web.sap.sd.payFlowBill.domain.UpdateCustParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.util.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 票据承兑服务
 */
@Service
@Slf4j
public class PayFlowBillService {
    @Resource
    private PayFlowBillMapper payFlowBillMapper;
    @Resource
    private ClientCore clientCore;

    /**
     * 查询列表
     */
    public List<PayFlowBill> selectList(ClientRpParam param) {
        return payFlowBillMapper.selectList(param);
    }

    /**
     * 查询详情
     */
    public PayFlowBill selectById(String sysBillNo) {
        return payFlowBillMapper.selectById(sysBillNo);
    }

    /**
     * 新增票据承兑
     */
    public void save(PayFlowBill bill) {
        // 校验数据唯一性
        checkDataAdd(bill);
        // 生成系统票号
        ClientResult numDoc = clientCore.tool.getNumDocB("Z000_PAY_BILL", "");
        bill.setSysBillNo(numDoc.getDocId());
        try {
            payFlowBillMapper.insert(bill);
        } catch (Exception e) {
            throw new ClientCustomException("保存失败");
        }
    }

    /**
     * 修改票据承兑
     */
    public void edit(PayFlowBill bill) {
        // 校验数据有效性
        checkData(bill);
        // 校验是否已认领
        checkDataReceipt(bill.getSysBillNo());
        try {
            payFlowBillMapper.update(bill);
        } catch (Exception e) {
            throw new ClientCustomException("更新失败");
        }
    }

    /**
     * 删除票据承兑
     */
    public void delete(String sysBillNo) {
        // 校验是否已认领
        checkDataReceipt(sysBillNo);
        payFlowBillMapper.deleteById(sysBillNo);
    }

    /**
     * 更新客户
     */
    public ClientResult updateCust(UpdateCustParam updateCustParam) {
        // 逐条校验是否已认领
        for (String sysId : updateCustParam.getSysId()) {
            checkDataReceipt(sysId);
        }
        ClientResult clientResult = new ClientResult();
        payFlowBillMapper.updateCust(updateCustParam.getSysId(), updateCustParam.getCust());
        clientResult.setStatus("S");
        clientResult.setMsg("更新客户成功");
        return clientResult;
    }

    /**
     * 导入数据（新增+更新）
     */
    public List<ClientResultMsgBox> importData(List<PayFlowBill> list) {
        List<ClientResultMsgBox> retBoxList = new ArrayList<>();
        List<PayFlowBill> insertList = new ArrayList<>();
        List<PayFlowBill> updateList = new ArrayList<>();
        try {
            // 逐行校验数据
            for (int i = 0; i < list.size(); i++) {
                PayFlowBill newBill = list.get(i);
                // 转换日期格式（YYYYMMDD → Date）
                newBill.setSignDate(ClientUtilDate.sapToDate(newBill.getSignDateStr()));
                newBill.setIssueDate(ClientUtilDate.sapToDate(newBill.getIssueDateStr()));
                newBill.setDueDate(ClientUtilDate.sapToDate(newBill.getDueDateStr()));
                checkImport(insertList, updateList, retBoxList, newBill, i);
            }
            // 存在校验错误则直接返回
            if (!CollectionUtils.isEmpty(retBoxList)) {
                return retBoxList;
            }
            // 转换票据类型和票据范围的编码格式
            convertBillTypeAndScope(insertList, updateList);
            // 批量新增
            if (CollectionUtils.isNotEmpty(insertList)) {
                insertList.forEach(i -> {
                    ClientResult numDoc = clientCore.tool.getNumDocB("Z000_PAY_BILL", "");
                    i.setSysBillNo(numDoc.getDocId());
                });
                payFlowBillMapper.batchInsert(insertList);
                retBoxList.add(new ClientResultMsgBox("3", "", "S", "导入成功，新增" + insertList.size() + "条数据"));
            }
            // 批量更新
            if (CollectionUtils.isNotEmpty(updateList)) {
                payFlowBillMapper.batchUpdate(updateList);
                retBoxList.add(new ClientResultMsgBox("3", "", "S", "导入成功，更新" + updateList.size() + "条数据"));
            }
        } catch (Exception e) {
            log.error("导入票据承兑数据失败", e);
            throw new ClientCustomException("数据导入失败: " + e.getMessage(), e);
        }
        return retBoxList;
    }

    /**
     * 校验导入数据，区分新增和更新
     */
    private void checkImport(List<PayFlowBill> insertList, List<PayFlowBill> updateList,
                             List<ClientResultMsgBox> retBoxList, PayFlowBill newBill, int index) {
        int rowNum = index + 1;
        // 必填字段校验
        if (StringUtil.isBlank(newBill.getBillNo())) {
            retBoxList.add(new ClientResultMsgBox("1", "", "E", "第" + rowNum + "行，票包号不能为空"));
        }
        if (StringUtil.isBlank(newBill.getSubBillFrom())) {
            retBoxList.add(new ClientResultMsgBox("1", "", "E", "第" + rowNum + "行，子票区间从不能为空"));
        }
        if (StringUtil.isBlank(newBill.getSubBillTo())) {
            retBoxList.add(new ClientResultMsgBox("1", "", "E", "第" + rowNum + "行，子票区间至不能为空"));
        }
        if (StringUtil.isBlank(newBill.getHolderAcc())) {
            retBoxList.add(new ClientResultMsgBox("1", "", "E", "第" + rowNum + "行，持票账户不能为空"));
        }
        if (StringUtil.isBlank(newBill.getEndorser())) {
            retBoxList.add(new ClientResultMsgBox("1", "", "E", "第" + rowNum + "行，前手账号名称不能为空"));
        }
        if (newBill.getAmt() == null) {
            retBoxList.add(new ClientResultMsgBox("1", "", "E", "第" + rowNum + "行，金额不能为空"));
        }
        if (newBill.getSignDate() == null) {
            retBoxList.add(new ClientResultMsgBox("1", "", "E", "第" + rowNum + "行，签收日期不能为空"));
        }
        if (StringUtil.isBlank(newBill.getBillType())) {
            retBoxList.add(new ClientResultMsgBox("1", "", "E", "第" + rowNum + "行，票据类型不能为空"));
        }
        if (StringUtil.isBlank(newBill.getBillScope())) {
            retBoxList.add(new ClientResultMsgBox("1", "", "E", "第" + rowNum + "行，票据范围不能为空"));
        }
        if (StringUtil.isBlank(newBill.getAcceptor())) {
            retBoxList.add(new ClientResultMsgBox("1", "", "E", "第" + rowNum + "行，承兑人不能为空"));
        }
        if (newBill.getIssueDate() == null) {
            retBoxList.add(new ClientResultMsgBox("1", "", "E", "第" + rowNum + "行，出票日期不能为空"));
        }
        if (newBill.getDueDate() == null) {
            retBoxList.add(new ClientResultMsgBox("1", "", "E", "第" + rowNum + "行，到期日期不能为空"));
        }
        // 存在校验错误则跳过该行
        if (!CollectionUtils.isEmpty(retBoxList)) {
            return;
        }
        // 根据持票账户+票包号+子区间判断是新增还是更新
        PayFlowBill billOld = payFlowBillMapper.selectByUk(newBill.getHolderAcc(), newBill.getBillNo(),
                newBill.getSubBillFrom(), newBill.getSubBillTo());
        if (billOld != null) {
            // 已存在认领金额的数据不允许导入
            if (billOld.getAmtReceipt().compareTo(BigDecimal.ZERO) > 0) {
                retBoxList.add(new ClientResultMsgBox("1", "", "E", "第" + rowNum + "行，已存在认领金额，不允许再导入"));
            } else {
                updateList.add(newBill);
            }
        } else {
            insertList.add(newBill);
        }
    }

    /**
     * 转换票据类型和票据范围编码（截取code.name中的code部分）
     */
    private void convertBillTypeAndScope(List<PayFlowBill> insertList, List<PayFlowBill> updateList) {
        if (CollectionUtils.isNotEmpty(insertList)) {
            insertList.forEach(this::processBillTypeAndScope);
        }
        if (CollectionUtils.isNotEmpty(updateList)) {
            updateList.forEach(this::processBillTypeAndScope);
        }
    }

    /**
     * 处理单个票据的类型和范围字段
     */
    private void processBillTypeAndScope(PayFlowBill bill) {
        if (bill.getBillType() != null && bill.getBillType().contains(".")) {
            bill.setBillType(bill.getBillType().split("\\.", 2)[0]);
        }
        if (bill.getBillScope() != null && bill.getBillScope().contains(".")) {
            bill.setBillScope(bill.getBillScope().split("\\.", 2)[0]);
        }
    }

    /**
     * 校验修改数据有效性
     */
    public void checkData(PayFlowBill bill) {
        clientCore.mdm.checkAE("CORE_CMPY", bill.getCmpy(), "", "0");
    }

    /**
     * 校验新增数据唯一性
     */
    public void checkDataAdd(PayFlowBill bill) {
        PayFlowBill exists = payFlowBillMapper.selectByUk(bill.getHolderAcc(), bill.getBillNo(),
                bill.getSubBillFrom(), bill.getSubBillTo());
        if (exists != null) {
            throw new ClientCustomException("持票账户、票包号、子区间已存在");
        }
        checkData(bill);
    }

    /**
     * 校验票据是否已认领
     */
    public void checkDataReceipt(String sysBillNo) {
        PayFlowBill exist = payFlowBillMapper.selectById(sysBillNo);
        if (exist == null) {
            throw new ClientCustomException("票据承兑数据不存在");
        }
        if (exist.getAmtReceipt().compareTo(BigDecimal.ZERO) > 0) {
            throw new ClientCustomException("票据已认领");
        }
    }
}
