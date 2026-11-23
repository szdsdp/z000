import { z000Tool } from "@/modules/common/indexCommon";
import { szdMsgBox } from "szd-client-web/client/components/msgBox/msgBox";

export function submitFormCheck(objData) {
  let msgList = [];
  submitFormCheckHeader(objData, msgList);

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
  z000Tool.checkNull(msgList, objData.header.mtlCode, "物料编码不能为空");
  z000Tool.checkNull(msgList, objData.header.fact, "工厂不能为空");
  z000Tool.checkNull(msgList, objData.header.orderType, "订单类型不能为空");
  z000Tool.checkNull(msgList, objData.header.startDate, "基本开始日期不能为空");
  z000Tool.checkNull(msgList, objData.header.endDate, "基本完成日期不能为空");
  z000Tool.checkNull(msgList, objData.header.quantity, "订单数量总计不能为空");
  z000Tool.checkNull(msgList, objData.header.unit, "单位不能为空");
}
