import { z000Tool } from "@/modules/common/indexCommon";
import { szdMsgBox } from "szd-client-web/client/components/msgBox/msgBox";

//检查数据
export function submitFormCheck(objData) {
  let msgList = [];
  submitFormCheckHeader(objData, msgList);
  submitFormCheckItemBkac(objData, msgList);
  submitFormCheckItemCmpy(objData, msgList);
  submitFormCheckItemSale(objData, msgList);
  submitFormCheckItemPurchase(objData, msgList);
  submitFormCheckItemCredit(objData, msgList);

  if (msgList.length === 0) {
    return true;
  } else {
    szdMsgBox(msgList).then();
    return false;
  }
}

// 检查抬头数据
function submitFormCheckHeader(objData, msgList) {
  if (!objData.header.custFlag || objData.header.custFlag.length === 0) {
    z000Tool.addMsg(msgList, "客商区分不能为空");
  }
  if (objData.header.custFlag.includes("K") && !objData.header.custFlag.includes("D")) {
    objData.cmpyDList = [];
    objData.purchaseList = [];
  }
  if (objData.header.custFlag.includes("D") && !objData.header.custFlag.includes("K")) {
    objData.cmpyKList = [];
    objData.saleList = [];
  }
  // SAP HEADER 必填字段
  z000Tool.checkNull(msgList, objData.header.custName, "客商名称不能为空");        // SAP NAME1
  z000Tool.checkNull(msgList, objData.header.custNameShort, "客商简称不能为空");   // SAP MCOD1
  z000Tool.checkNull(msgList, objData.header.custGrp, "客商分组不能为空");         // SAP BU_GROUP
  z000Tool.checkNull(msgList, objData.header.partnerType, "伙伴类型不能为空");      // SAP BPKIND
  z000Tool.checkNull(msgList, objData.header.ctry, "国家不能为空");               // SAP LAND1
  z000Tool.checkNull(msgList, objData.header.province, "省份不能为空");           // SAP REGIO
  z000Tool.checkNull(msgList, objData.header.city, "城市不能为空");               // SAP ORT01
  z000Tool.checkNull(msgList, objData.header.postCode, "邮政编码不能为空");
  z000Tool.checkNull(msgList, objData.header.address, "详细地址不能为空");         // SAP STRAS
  z000Tool.checkNull(msgList, objData.header.phone, "电话不能为空");              // SAP TELF1
  z000Tool.checkNull(msgList, objData.header.contact, "联系人不能为空");           // SAP REMARK
  z000Tool.checkNull(msgList, objData.header.idNum, "税号/身份证不能为空");        // SAP STCEG
  z000Tool.checkNull(msgList, objData.header.taxClass, "税分类不能为空");         // SAP TAXKD
  z000Tool.checkNull(msgList, objData.header.riskClass, "风险类不能为空");        // SAP RISK_CLASS
}

// 检查银行视图
function submitFormCheckItemBkac(objData, msgList) {
  // if (objData.bkacList.length === 0) {
  //   z000Tool.addMsg(msgList, "银行视图不能为空");
  //   return;
  // }
  for (let i = 0; i < objData.bkacList.length; i++) {
    let item = objData.bkacList[i];
    z000Tool.checkNull(msgList, item.acctNo, "银行视图-银行账号不能为空");
    z000Tool.checkNull(msgList, item.acctName, "银行视图-账户名称不能为空");
    z000Tool.checkNull(msgList, item.cnap, "银行视图-开户行不能为空");
  }
}

// 检查公司视图
function submitFormCheckItemCmpy(objData, msgList) {
  if (objData.header.custFlag.includes("K") && objData.cmpyKList.length === 0) {
    z000Tool.addMsg(msgList, "客户公司视图不能为空");
    return;
  }
  if (objData.header.custFlag.includes("D") && objData.cmpyDList.length === 0) {
    z000Tool.addMsg(msgList, "供应商公司视图不能为空");
    return;
  }
  for (let i = 0; i < objData.cmpyKList.length; i++) {
    let item = objData.cmpyKList[i];
    // SAP ITEM_KNB1 必填字段: BUKRS, AKONT, ZTERM
    z000Tool.checkNull(msgList, item.cmpy, "客户公司视图-公司代码不能为空");
    z000Tool.checkNull(msgList, item.accs, "客户公司视图-统驭科目不能为空");
    z000Tool.checkNull(msgList, item.payTerms, "客户公司视图-付款条件不能为空");
  }
  for (let i = 0; i < objData.cmpyDList.length; i++) {
    let item = objData.cmpyDList[i];
    z000Tool.checkNull(msgList, item.cmpy, "供应商公司视图-公司代码不能为空");
    z000Tool.checkNull(msgList, item.accs, "供应商公司视图-统驭科目不能为空");
    z000Tool.checkNull(msgList, item.payTerms, "供应商公司视图-付款条件不能为空");
    // z000Tool.checkNull(msgList, item.payType, "供应商公司视图-付款方式不能为空");
  }
}

// 检查销售视图
function submitFormCheckItemSale(objData, msgList) {
  // if (objData.header.custFlag.includes("K") && objData.saleList.length === 0) {
  //   z000Tool.addMsg(msgList, "客户销售视图不能为空");
  //   return;
  // }
  for (let i = 0; i < objData.saleList.length; i++) {
    let item = objData.saleList[i];
    // SAP ITEM_KNVV 必填字段: VKORG, VTWEG, SPART, WAERS
    z000Tool.checkNull(msgList, item.saleOrg, "客户销售视图-销售组织不能为空");
    z000Tool.checkNull(msgList, item.vtweg, "客户销售视图-分销渠道不能为空");
    z000Tool.checkNull(msgList, item.spart, "客户销售视图-产品组不能为空");
    z000Tool.checkNull(msgList, item.curr, "客户销售视图-币种不能为空");
    // if (item.partnerList.length === 0) {
    //   z000Tool.addMsg(msgList, "客户业务伙伴不能为空");
    //   return;
    // }
    // const hasZY = item.partnerList.some(item => item.partnerType === "ZY");
    // if (!hasZY) {
    //   z000Tool.addMsg(msgList, "客户销售视图-业务伙伴-区域负责人不能为空");
    //   return;
    // }
    // const hasZZ = item.partnerList.some(item => item.partnerType === "ZZ");
    // if (!hasZZ) {
    //   z000Tool.addMsg(msgList, "客户销售视图-业务伙伴-销售经理不能为空");
    //   return;
    // }
  }
}

// 检查采购视图
function submitFormCheckItemPurchase(objData, msgList) {
  if (objData.header.custFlag.includes("D") && objData.purchaseList.length === 0) {
    z000Tool.addMsg(msgList, "供应商采购视图不能为空");
    return;
  }
  for (let i = 0; i < objData.purchaseList.length; i++) {
    let item = objData.purchaseList[i];
    z000Tool.checkNull(msgList, item.purchaseOrg, "供应商采购视图-采购组织不能为空");
    z000Tool.checkNull(msgList, item.curr, "供应商采购视图-订单货币不能为空");
    z000Tool.checkNull(msgList, item.payTerms, "供应商采购视图-付款条件不能为空");
  }
}

// 检查信用视图
function submitFormCheckItemCredit(objData, msgList) {
  // if (objData.header.creditFlag === "0" && objData.creditList.length === 0) {
  //   z000Tool.addMsg(msgList, "信用视图不能为空");
  //   return;
  // }
  for (let i = 0; i < objData.creditList.length; i++) {
    let item = objData.creditList[i];
    z000Tool.checkNull(msgList, item.creditSgmnt, "信用视图-信用段不能为空");
    z000Tool.checkNull(msgList, item.creditLimit, "信用视图-信用额度不能为空");
  }
}
