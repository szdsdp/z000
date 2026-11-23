package com.szd.z000.web.sap.mdm.bankAcc.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientResultMsgBox;
import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.core.client.exception.ClientCustomException;
import com.szd.z000.base.orm.domain.mdm.bankAcc.BankAcc;
import com.szd.z000.base.orm.mapper.mdm.bankAcc.BankAccMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.util.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 本方账户服务
 */
@Service
@Slf4j
public class BankAccService {
    @Resource
    private BankAccMapper bankAccMapper;
    @Resource
    private ClientCore clientCore;

    /**
     * 查询列表
     */
    public List<BankAcc> selectList(ClientRpParam param) {
        return bankAccMapper.selectList(param);
    }

    /**
     * 查询详情
     */
    public BankAcc selectById(String accNo) {
        return bankAccMapper.selectById(accNo);
    }

    /**
     * 新增本方账户
     */
    public void save(BankAcc bankAcc) {
        // 检查是否已存在
        BankAcc exist = bankAccMapper.selectById(bankAcc.getAccNo());
        if (!ObjectUtils.isEmpty(exist)) {
            throw new ClientCustomException("公司代码、银行账户已存在");
        }
        // 校验公司代码和会计科目是否存在
        clientCore.mdm.checkAE("CORE_CMPY", bankAcc.getCmpy(), "", "0");
        clientCore.mdm.checkAE("CORE_ACCS", bankAcc.getAccs(), "", "0");
        bankAccMapper.insert(bankAcc);
    }

    /**
     * 修改本方账户
     */
    public void edit(BankAcc bankAcc) {
        bankAccMapper.update(bankAcc);
    }

    /**
     * 删除本方账户
     */
    public void delete(String accNo) {
        bankAccMapper.deleteById(accNo);
    }

    /**
     * 导入数据（新增+更新）
     */
    public List<ClientResultMsgBox> importData(List<BankAcc> list) {
        List<ClientResultMsgBox> retBoxList = new ArrayList<>();
        List<BankAcc> insertList = new ArrayList<>();
        List<BankAcc> updateList = new ArrayList<>();
        try {
            // 逐行校验数据
            for (int i = 0; i < list.size(); i++) {
                checkImport(insertList, updateList, retBoxList, list.get(i), i);
            }
            // 存在校验错误则直接返回
            if (CollectionUtils.isNotEmpty(retBoxList)) {
                return retBoxList;
            }
            // 批量新增
            if (CollectionUtils.isNotEmpty(insertList)) {
                bankAccMapper.batchInsert(insertList);
                retBoxList.add(new ClientResultMsgBox("3", "", "S", "导入成功，新增" + insertList.size() + "条数据"));
            }
            // 批量更新
            if (CollectionUtils.isNotEmpty(updateList)) {
                for (BankAcc item : updateList) {
                    bankAccMapper.update(item);
                }
                retBoxList.add(new ClientResultMsgBox("3", "", "S", "导入成功，更新" + updateList.size() + "条数据"));
            }
        } catch (Exception e) {
            log.error("导入本方账户数据失败", e);
            throw new ClientCustomException("数据导入失败: " + e.getMessage(), e);
        }
        return retBoxList;
    }

    /**
     * 校验导入数据，区分新增和更新
     */
    private void checkImport(List<BankAcc> insertList, List<BankAcc> updateList,
                             List<ClientResultMsgBox> retBoxList, BankAcc newBankAcc, int index) {
        int rowNum = index + 1;
        // 必填字段校验
        if (StringUtil.isBlank(newBankAcc.getCmpy())) {
            retBoxList.add(new ClientResultMsgBox("1", "", "E", "第" + rowNum + "行，公司代码不能为空"));
        }
        if (StringUtil.isBlank(newBankAcc.getAccNo())) {
            retBoxList.add(new ClientResultMsgBox("1", "", "E", "第" + rowNum + "行，银行账户不能为空"));
        }
        // 存在校验错误则跳过该行
        if (CollectionUtils.isNotEmpty(retBoxList)) {
            return;
        }
        // 检查导入数据中是否已存在相同的银行账户和公司代码组合
        boolean isDuplicateInInsert = insertList.stream()
                .anyMatch(item -> item.getAccNo().equals(newBankAcc.getAccNo()) && item.getCmpy().equals(newBankAcc.getCmpy()));
        boolean isDuplicateInUpdate = updateList.stream()
                .anyMatch(item -> item.getAccNo().equals(newBankAcc.getAccNo()) && item.getCmpy().equals(newBankAcc.getCmpy()));
        if (isDuplicateInInsert || isDuplicateInUpdate) {
            retBoxList.add(new ClientResultMsgBox("1", "", "E", "第" + rowNum + "行，已存在相同的银行账户和公司代码组合，不允许重复导入"));
            return;
        }
        // 根据银行账户判断是新增还是更新
        BankAcc bankAccOld = bankAccMapper.selectById(newBankAcc.getAccNo());
        if (bankAccOld != null) {
            updateList.add(newBankAcc);
        } else {
            insertList.add(newBankAcc);
        }
    }
}
