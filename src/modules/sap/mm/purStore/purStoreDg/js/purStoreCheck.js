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
  z000Tool.checkNull(msgList, objData.header.cstc, "部门不能为空");
  z000Tool.checkNull(msgList, objData.header.postDate, "凭证过帐日期不能为空");
  z000Tool.checkNull(msgList, objData.header.vchDate, "凭证日期不能为空");
  z000Tool.checkNull(msgList, objData.header.title, "业务标题不能为空");
}

function submitFormCheckItem(objData, msgList) {
  if (!objData.itemList || objData.itemList.length === 0) {
    z000Tool.addMsg(msgList, "行项目不能为空");
    return;
  }
  for (let i = 0; i < objData.itemList.length; i++) {
    let item = objData.itemList[i];
    z000Tool.checkNull(msgList, item.fact, "行项目[" + item.itemNo + "]-工厂不能为空");
    z000Tool.checkNull(msgList, item.store, "行项目[" + item.itemNo + "]-库存地点不能为空");
    z000Tool.checkNull(msgList, item.quantity, "行项目[" + item.itemNo + "]-数量不能为空");
    z000Tool.checkNull(msgList, item.unit, "行项目[" + item.itemNo + "]-单位不能为空");
    z000Tool.checkNull(msgList, item.orderNo, "行项目[" + item.itemNo + "]-订单号不能为空");
    z000Tool.checkNull(msgList, item.orderItemNo, "行项目[" + item.itemNo + "]-订单项目编号不能为空");
  }
}
