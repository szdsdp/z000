package com.szd.z000.web.sap.mm.purStore.enums;

/**
 * 入库业务类型枚举
 */
public enum PurStoreBstpEnum {

    /**
     * 物料凭证
     */
    CGRK01("Z000_MM_CGRK01", "01", "101"),
    /**
     * 生产投料
     */
    CGRK02("Z000_MM_CGRK02", "03", "261"),
    /**
     * 生产订单入库
     */
    CGRK03("Z000_MM_CGRK03", "02", "101");

    private final String bstp;
    private final String gmCode;
    private final String moveType;

    PurStoreBstpEnum(String bstp, String gmCode, String moveType) {
        this.bstp = bstp;
        this.gmCode = gmCode;
        this.moveType = moveType;
    }

    public String getBstp() {
        return bstp;
    }

    public String getGmCode() {
        return gmCode;
    }

    public String getMoveType() {
        return moveType;
    }

    /**
     * 根据业务类型编码匹配枚举，未匹配时默认返回物料凭证
     */
    public static PurStoreBstpEnum getByBstp(String bstp) {
        for (PurStoreBstpEnum e : values()) {
            if (e.bstp.equals(bstp)) {
                return e;
            }
        }
        return CGRK01;
    }
}
