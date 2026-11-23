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
  z000Tool.checkNull(msgList, objData.header.mtlName, "物料名称不能为空");
  z000Tool.checkNull(msgList, objData.header.indSector, "行业领域不能为空");
  z000Tool.checkNull(msgList, objData.header.mtlType, "物料类型不能为空");
}
