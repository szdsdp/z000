import { z000Tool } from "@/modules/common/indexCommon";
import { szdMsgBox } from "szd-client-app/client/components/msgBox/msgBox";

// 检查提交数据
export function submitFormCheck(objData) {
  let msgList = [];
  // 检查抬头数据
  submitFormCheckHeader(objData, msgList);
  // 检查项目数据
  submitFormCheckItem(objData, msgList);

  if (msgList.length === 0) {
    return true;
  } else {
    szdMsgBox(msgList).then();
    return false;
  }
}

// 检查抬头数据
function submitFormCheckHeader(objData, msgList) {
  z000Tool.checkNull(msgList, objData.header.applyType, "申请类型不能为空");
  z000Tool.checkNull(msgList, objData.header.cmpy, "公司代码不能为空");
  z000Tool.checkNull(msgList, objData.header.cstc, "部门不能为空");
}

// 检查项目数据
function submitFormCheckItem(objData, msgList) {
  if (!objData.itemList || objData.itemList.length === 0) {
    z000Tool.addMsg(msgList, "行项目不能为空");
    return;
  }
  for (let i = 0; i < objData.itemList.length; i++) {
    let item = objData.itemList[i];
    z000Tool.checkNull(msgList, item.mtlCode, "行项目[" + item.itemNo + "]-物料编码不能为空");
    z000Tool.checkNull(msgList, item.fact, "行项目[" + item.itemNo + "]-工厂不能为空");
    z000Tool.checkNull(msgList, item.applyNum, "行项目[" + item.itemNo + "]-采购申请数量不能为空");
    z000Tool.checkNull(msgList, item.unit, "行项目[" + item.itemNo + "]-单位不能为空");
    z000Tool.checkNull(msgList, item.needDate, "行项目[" + item.itemNo + "]-需求日期不能为空");
    z000Tool.checkNull(msgList, item.deliveryDate, "行项目[" + item.itemNo + "]-交货日期不能为空");
  }
}
