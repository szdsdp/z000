package com.szd.z000.web.sap.mdm.mtlcj.domain;

import com.szd.z000.base.orm.domain.mdm.mtlcj.MtlcjBaseView;
import com.szd.z000.base.orm.domain.mdm.mtlcj.MtlcjHeader;
import com.szd.z000.base.orm.domain.mdm.mtlcj.MtlcjPurView;
import lombok.Data;

/**
 * 物料主数据创建申请-数据VO
 */
@Data
public class MtlcjDataVO {
    /** 基本信息 */
    private MtlcjHeader header;
    /** 基础视图 */
    private MtlcjBaseView baseView;
    /** 采购视图 */
    private MtlcjPurView purView;
}
