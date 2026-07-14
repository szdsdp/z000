package com.szd.z000.base.orm.mapper.demo.mdm;

import com.szd.z000.base.orm.domain.demo.mdm.MdmCmpy;

import java.util.List;

/**
 * 公司代码Mapper接口
 */
public interface MdmCmpyMapper {
    List<MdmCmpy> selectList(MdmCmpy cmpy);
    MdmCmpy selectById(String cmpy);
    void insert(MdmCmpy cmpy);
    void update(MdmCmpy cmpy);
    void deleteByIds(String[] cmpys);
    List<MdmCmpy> selectCmpyCommonList(MdmCmpy cmpy);
}
