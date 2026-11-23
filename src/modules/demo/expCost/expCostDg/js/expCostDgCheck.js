import { szdMsgBox } from "szd-client-web/client/components/msgBox/msgBox";
import { $mListNull } from "szd-client-web/client/common/utils/globalConfig";

//检查提交数据
export function submitFormCheck(objData) {
  let msgList = [];
  // 检查抬头数据
  submitFormCheckHead(objData, msgList);
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
function submitFormCheckHead(objData, msgList) {
  $mListNull(msgList, objData.header.cmpy, "公司不能为空");
  $mListNull(msgList, objData.header.cstc, "部门不能为空");
  $mListNull(msgList, objData.header.bstp, "业务类型不能为空");
  $mListNull(msgList, objData.header.bussDate, "业务日期不能为空");
  $mListNull(msgList, objData.header.userId, "报账人不能为空");
  $mListNull(msgList, objData.header.title, "业务标题不能为空");
}

// 检查项目数据
function submitFormCheckItem(objData, msgList) {
  for (let i = 0; i < objData.itemList.length; i++) {
    const item = objData.itemList[i];
    $mListNull(msgList, item.bsub, ["项目信息-", i + 1, "行业务小类不能为空"]);
    $mListNull(msgList, item.taxr, ["项目信息-", i + 1, "税率不能为空"]);
    $mListNull(msgList, item.amt, ["项目信息-", i + 1, "金额不能为空"]);
  }
}