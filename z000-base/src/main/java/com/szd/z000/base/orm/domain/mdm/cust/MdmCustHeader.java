package com.szd.z000.base.orm.domain.mdm.cust;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.szd.core.client.domain.wf.ClientWfBussBase;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 客商申请-抬头
 */
@Data
public class MdmCustHeader extends ClientWfBussBase {
    /**
     * 客商申请ID
     */
    private String applyId;
    /**
     * 客商区分(K-客户；D-供应商)
     */
    private List<String> custFlag;
    private String custFlagDB;
    /**
     * 客商编码
     */
    private String custId;
    private String custName;
    private String custNameShort;
    /**
     * 客商分组
     */
    private String custGrp;
    private String custGrpName;
    /**
     * 伙伴类型
     */
    private String partnerType;
    private String partnerTypeName;
    /**
     * U8旧编码
     */
    private String u8Code;
    /**
     * 国家
     */
    private String ctry;
    private String ctryName;
    /**
     * 省
     */
    private String province;
    private String provinceCore;
    private String provinceName;
    /**
     * 城市
     */
    private String city;
    private String cityName;
    /**
     * 详细地址-街道
     */
    private String address;
    /**
     * 电话
     */
    private String phone;
    /**
     * 联系人
     */
    private String contact;
    /**
     * 税号类别
     */
    private String taxType;
    private String taxTypeName;
    /**
     * 税号
     */
    private String idNum;
    /**
     * 风险类销售
     */
    private String riskClass;
    private String riskClassName;
    /**
     * 税分类
     */
    private String taxClass;
    private String taxClassName;
    /**
     * 营业执照
     */
    private String fileObjId;
    /**
     * 业务日期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date bussDate;
    /**
     * 业务状态
     */
    private String bussStatus;
    private String bussStatusName;

    /**
     * 信用启用标识
     */
    private String creditFlag;

    private String sapMsg;
    private String sapMessId;

    private String regionCode;

    /**
     * 合作开始日期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date partStartDate;
    /**
     * 合作结束日期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date partEndDate;

    /***
     * 邮政编码
     */
    private String postCode;
}
