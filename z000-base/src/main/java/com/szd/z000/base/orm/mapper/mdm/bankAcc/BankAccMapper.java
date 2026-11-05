package com.szd.z000.base.orm.mapper.mdm.bankAcc;

import com.szd.core.client.domain.common.ClientRpParam;
import com.szd.z000.base.orm.domain.mdm.bankAcc.BankAcc;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BankAccMapper {
    List<BankAcc> selectList(ClientRpParam param);

    BankAcc selectById(@Param("accNo") String accNo);

    void deleteById(String accNo);

    void batchDeleteByIds(List<String> accNo);

    void batchInsert(List<BankAcc> list);

    void insert(BankAcc bankAcc);

    void update(BankAcc bankAcc);
}
