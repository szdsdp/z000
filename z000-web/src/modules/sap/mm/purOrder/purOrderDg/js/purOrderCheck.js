import { z000Tool } from "@/modules/common/indexCommon";
import { szdMsgBox } from "szd-client-web/client/components/msgBox/msgBox";

// 检查数据
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

// 检查抬头数据
function submitFormCheckHeader(objData, msgList) {
  z000Tool.checkNull(msgList, objData.header.vchType, "采购凭证类型不能为空");
  z000Tool.checkNull(msgList, objData.header.cmpy, "公司代码不能为空");
  z000Tool.checkNull(msgList, objData.header.supp, "供应商账号不能为空");
  z000Tool.checkNull(msgList, objData.header.purOrg, "采购组织不能为空");
  z000Tool.checkNull(msgList, objData.header.purGroup, "采购组不能为空");
  z000Tool.checkNull(msgList, objData.header.curr, "货币码不能为空");
  z000Tool.checkNull(msgList, objData.header.cstc, "成本中心不能为空");
  z000Tool.checkNull(msgList, objData.header.vchDate, "采购凭证日期不能为空");
  z000Tool.checkNull(msgList, objData.header.textLine, "抬头文本不能为空");
}

// 检查行项目数据
function submitFormCheckItem(objData, msgList) {
  if (!objData.itemList || objData.itemList.length === 0) {
    z000Tool.addMsg(msgList, "行项目不能为空");
    return;
  }
  for (let i = 0; i < objData.itemList.length; i++) {
    let item = objData.itemList[i];
    z000Tool.checkNull(msgList, item.mtlCode, "行项目[" + item.itemNo + "]-物料编码不能为空");
    z000Tool.checkNull(msgList, item.fact, "行项目[" + item.itemNo + "]-工厂不能为空");
    z000Tool.checkNull(msgList, item.purNum, "行项目[" + item.itemNo + "]-采购订单数量不能为空");
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

  // 校验每个行项目必须有至少一条交货计划
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
      z000Tool.checkNull(msgList, sched.deliveryDate, "行项目[" + item.itemNo + "]-交货计划行[" + sched.schedLine + "]-交货日期不能为空");
      z000Tool.checkNull(msgList, sched.quantity, "行项目[" + item.itemNo + "]-交货计划行[" + sched.schedLine + "]-已计划数量不能为空");
      sumQty += parseFloat(sched.quantity) || 0;
    }
    // 校验：交货计划已计划数量总和不能超过采购订单数量
    let purNum = parseFloat(item.purNum) || 0;
    if (sumQty > purNum) {
      z000Tool.addMsg(msgList, "行项目[" + item.itemNo + "]-交货计划已计划数量合计(" + sumQty + ")不能超过采购订单数量(" + purNum + ")");
    }
  }
}

