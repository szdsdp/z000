import { z000Tool } from "@/modules/common/indexCommon";
import { szdMsgBox } from "szd-client-app/client/components/msgBox/msgBox";

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
  z000Tool.checkNull(msgList, objData.header.type, "类型不能为空");
  z000Tool.checkNull(msgList, objData.header.cmpy, "公司代码不能为空");
  z000Tool.checkNull(msgList, objData.header.cstc, "部门不能为空");
  z000Tool.checkNull(msgList, objData.header.supp, "供应商不能为空");
  z000Tool.checkNull(msgList, objData.header.purOrg, "采购组织不能为空");
  z000Tool.checkNull(msgList, objData.header.purGroup, "采购组不能为空");
  z000Tool.checkNull(msgList, objData.header.curr, "货币不能为空");
  z000Tool.checkNull(msgList, objData.header.startDate, "有效开始日期不能为空");
  z000Tool.checkNull(msgList, objData.header.endDate, "有效结束日期不能为空");
}

function submitFormCheckItem(objData, msgList) {
  if (!objData.itemList || objData.itemList.length === 0) {
    z000Tool.addMsg(msgList, "行项目不能为空");
    return;
  }
  for (let i = 0; i < objData.itemList.length; i++) {
    let item = objData.itemList[i];
    z000Tool.checkNull(msgList, item.mtlCode, "行项目[" + item.itemNo + "]-物料编码不能为空");
    z000Tool.checkNull(msgList, item.itemText, "行项目[" + item.itemNo + "]-短文本不能为空");
    z000Tool.checkNull(msgList, item.fact, "行项目[" + item.itemNo + "]-工厂不能为空");
    z000Tool.checkNull(msgList, item.quantity, "行项目[" + item.itemNo + "]-目标数量不能为空");
    z000Tool.checkNull(msgList, item.unit, "行项目[" + item.itemNo + "]-单位不能为空");
    z000Tool.checkNull(msgList, item.price, "行项目[" + item.itemNo + "]-净价不能为空");
  }
}
