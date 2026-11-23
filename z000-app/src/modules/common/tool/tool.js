import { $t } from "szd-client-app/client/common/utils/globalConfig";

/**
 * 检查空信息
 * @param {Object}  msgList 消息列表
 * @param {any}  val 任意值
 * @param {String}  msg 消息内容
 */
export function checkNull(msgList, val, msg) {
  if (!val) {
    msgList.push({ light: "1", retCode: "E", retMsg: $t(msg) });
  }
}

/**
 * 添加消息
 * @param {Object}  msgList 消息列表
 * @param {String}  msg 消息内容
 */
export function addMsg(msgList, msg) {
  msgList.push({ light: "1", retCode: "E", retMsg: $t(msg) });
}
