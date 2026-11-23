import { z000Tool } from "@/modules/common/indexCommon";
import { szdMsgBox } from "szd-client-app/client/components/msgBox/msgBox";

// 检查提交数据
export function submitFormCheck(objData) {
  let msgList = [];
  // 检查项目数据
  submitFormCheckItem(objData, msgList);

  if (msgList.length === 0) {
    return true;
  } else {
    szdMsgBox(msgList).then();
    return false;
  }
}

// 检查项目数据
function submitFormCheckItem(objData, msgList) {
  if (!objData.itemList || objData.itemList.length === 0) {
    z000Tool.addMsg(msgList, "行项目不能为空");
    return;
  }
  for (let i = 0; i < objData.itemList.length; i++) {
    let item = objData.itemList[i];
    z000Tool.checkNull(msgList, item.countQnt, "行项目[" + item.itemNo + "]-盘点数量不能为空");
    z000Tool.checkNull(msgList, item.counter, "行项目[" + item.itemNo + "]-盘点人不能为空");
  }
}
