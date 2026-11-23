import { z000Tool } from "@/modules/common/indexCommon";
import { szdMsgBox } from "szd-client-web/client/components/msgBox/msgBox";

export function submitFormCheck(objData) {
  let msgList = [];
  submitFormCheckHead(objData, msgList);
  if (["10", "30"].includes(objData.header.payNature)) {
    submitFormCheckItemClear(objData, msgList);
  } else if (["20", "40"].includes(objData.header.payNature)) {
    submitFormCheckItemSale(objData, msgList);
  }

  if (msgList.length === 0) {
    return true;
  } else {
    szdMsgBox(msgList).then();
    return false;
  }
}

// 检查抬头数据
function submitFormCheckHead(objData, msgList) {
  z000Tool.checkNull(msgList, objData.header.payNature, "回款性质不能为空");
  z000Tool.checkNull(msgList, objData.header.prfc, "利润中心不能为空");
  z000Tool.checkNull(msgList, objData.header.amtReceipt, "本次认领金额不能为空");
  z000Tool.checkNull(msgList, objData.header.text, "项目文本不能为空");
  z000Tool.checkNull(msgList, objData.header.title, "业务标题不能为空");
  if (!objData.header.amtReceipt) objData.header.amtReceipt = 0;
  if (!objData.header.amtRemain) objData.header.amtRemain = 0;
  if (objData.header.amtReceipt <= 0) {
    z000Tool.addMsg(msgList, "本次认领金额不能为空,且必须大于0");
  } else if (objData.header.amtReceipt > objData.header.amtRemain) {
    z000Tool.addMsg(msgList, "本次认领金额不能大于剩余认领金额");
  }
}

// 检查项目信息-未清项
function submitFormCheckItemClear(objData, msgList) {
  if (objData.itemList.length === 0) {
    z000Tool.addMsg(msgList, "未清信息不能为空");
    return;
  }
  let totalClearAmt = 0;
  for (let i = 0; i < objData.itemList.length; i++) {
    let item = objData.itemList[i];
    if (!item.clearAmt || item.clearAmt === 0) {
      z000Tool.addMsg(msgList, "未清信息-清账金额不能为空");
      return;
    }
    if ((item.amt > 0 && item.clearAmt > item.amt) || (item.amt < 0 && item.clearAmt < item.amt)) {
      z000Tool.addMsg(msgList, "未清信息-清账金额不能大于凭证货币金额");
      return;
    }
    totalClearAmt += Number(item.clearAmt || 0);
  }
  if (totalClearAmt.toFixed(2) != objData.header.amtReceipt.toFixed(2)) {
    z000Tool.addMsg(msgList, "未清信息-汇总清账金额与抬头认款金额不一致");
  }
}

// 检查项目信息-销售合同
function submitFormCheckItemSale(objData, msgList) {
  if (objData.itemListSale.length === 0) {
    z000Tool.addMsg(msgList, "销售合同信息不能为空");
    return;
  }
  let totalClearAmt = 0;
  for (let i = 0; i < objData.itemListSale.length; i++) {
    let item = objData.itemListSale[i];
    if (!item.clearAmt || item.clearAmt <= 0) {
      z000Tool.addMsg(msgList, "销售合同-认领金额不能为空，且必须大于0");
      return;
    }
    totalClearAmt += Number(item.clearAmt || 0);
  }
  if (totalClearAmt.toFixed(2) != objData.header.amtReceipt.toFixed(2)) {
    z000Tool.addMsg(msgList, "销售合同-汇总认款金额与抬头认款金额不一致");
  }
}
