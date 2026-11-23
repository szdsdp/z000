import { z000Tool } from "@/modules/common/indexCommon";
import { szdMsgBox } from "szd-client-web/client/components/msgBox/msgBox";

export function submitFormCheck(objData) {
  let msgList = [];
  submitFormCheckHeader(objData, msgList);
  submitFormCheckItem(objData, msgList);
  submitFormCheckItemSub(objData, msgList);

  if (msgList.length === 0) {
    return true;
  } else {
    szdMsgBox(msgList).then();
    return false;
  }
}

function submitFormCheckHeader(objData, msgList) {
  z000Tool.checkNull(msgList, objData.header.cmpy, "公司代码不能为空");
  z000Tool.checkNull(msgList, objData.header.cstc, "成本中心不能为空");
  z000Tool.checkNull(msgList, objData.header.soldTo, "售达方不能为空");
  z000Tool.checkNull(msgList, objData.header.orderType, "订单类型不能为空");
  z000Tool.checkNull(msgList, objData.header.salesOrg, "销售机构不能为空");
  z000Tool.checkNull(msgList, objData.header.distrChan, "分销渠道不能为空");
  z000Tool.checkNull(msgList, objData.header.prodGroup, "产品组不能为空");
}

function submitFormCheckItem(objData, msgList) {
  if (!objData.itemList || objData.itemList.length === 0) {
    z000Tool.addMsg(msgList, "行项目不能为空");
    return;
  }
  for (let i = 0; i < objData.itemList.length; i++) {
    let item = objData.itemList[i];
    z000Tool.checkNull(msgList, item.mtlCode, "行项目[" + item.itemNo + "]-物料编码不能为空");
    z000Tool.checkNull(msgList, item.entryQnt, "行项目[" + item.itemNo + "]-数量不能为空");
    z000Tool.checkNull(msgList, item.unit, "行项目[" + item.itemNo + "]-单位不能为空");
    z000Tool.checkNull(msgList, item.deliveryDate, "行项目[" + item.itemNo + "]-交货日期不能为空");
  }
}

// 检查行项目数据-交货计划
function submitFormCheckItemSub(objData, msgList) {
  if (!objData.itemList || objData.itemList.length === 0) {
    return;
  }
  if (!objData.schedList) objData.schedList = [];

  for (let i = 0; i < objData.itemList.length; i++) {
    let item = objData.itemList[i];
    let itemScheds = objData.schedList.filter(s => s.itemNo === item.itemNo);
    if (itemScheds.length === 0) {
      z000Tool.addMsg(msgList, "行项目[" + item.itemNo + "]-交货计划不能为空");
      continue;
    }
    // 校验每条交货计划的必填字段 + 汇总数量
    let sumQty = 0;
    for (let j = 0; j < itemScheds.length; j++) {
      let sched = itemScheds[j];
      z000Tool.checkNull(msgList, sched.reqDate, "行项目[" + item.itemNo + "]计划[" + sched.schedNo + "]-计划到货日期不能为空");
      z000Tool.checkNull(msgList, sched.reqQty, "行项目[" + item.itemNo + "]计划[" + sched.schedNo + "]-数量不能为空");
      sumQty += parseFloat(sched.reqQty) || 0;
    }
    // 校验：交货计划数量合计不能超过订单数量
    let entryQnt = parseFloat(item.entryQnt) || 0;
    if (sumQty > entryQnt) {
      z000Tool.addMsg(msgList, "行项目[" + item.itemNo + "]-交货计划数量合计(" + sumQty + ")不能超过订单数量(" + entryQnt + ")");
    }
  }
}
