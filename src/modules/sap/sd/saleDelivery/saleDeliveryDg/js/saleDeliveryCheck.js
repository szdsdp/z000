import { z000Tool } from "@/modules/common/indexCommon";
import { szdMsgBox } from "szd-client-web/client/components/msgBox/msgBox";

export function submitFormCheck(objData) {
  let msgList = [];
  submitFormCheckHeader(objData, msgList);
  submitFormCheckItem(objData, msgList);

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
  z000Tool.checkNull(msgList, objData.header.shipPoint, "装运点不能为空");
  z000Tool.checkNull(msgList, objData.header.deliveryDeadline, "截至交货到期日不能为空");
}

function submitFormCheckItem(objData, msgList) {
  if (!objData.itemList || objData.itemList.length === 0) {
    z000Tool.addMsg(msgList, "行项目不能为空");
    return;
  }
  for (let i = 0; i < objData.itemList.length; i++) {
    let item = objData.itemList[i];
    z000Tool.checkNull(msgList, item.orderNo, "行项目[" + item.itemNo + "]-销售订单号不能为空");
    z000Tool.checkNull(msgList, item.orderItemNo, "行项目[" + item.itemNo + "]-销售订单行号不能为空");
    z000Tool.checkNull(msgList, item.mtlCode, "行项目[" + item.itemNo + "]-物料编码不能为空");
    z000Tool.checkNull(msgList, item.orderQty, "行项目[" + item.itemNo + "]-订单数量不能为空");
    z000Tool.checkNull(msgList, item.dlvQty, "行项目[" + item.itemNo + "]-交货数量不能为空");
    z000Tool.checkNull(msgList, item.unit, "行项目[" + item.itemNo + "]-单位不能为空");
    // 校验交货数量不能超过未交货数量
    if (item.dlvQty && item.undeliveredQty) {
      let dlvQty = parseFloat(item.dlvQty);
      let undeliveredQty = parseFloat(item.undeliveredQty);
      if (dlvQty > undeliveredQty) {
        z000Tool.addMsg(msgList, "行项目[" + item.itemNo + "]-交货数量不能超过未交货数量(" + undeliveredQty + ")");
      }
    }
  }
}
