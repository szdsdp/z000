import { $mE, $t } from "szd-client-web/client/common/utils/globalConfig";

/**
 * 检查空信息
 * @param {Object}  msgList 消息列表 { light:指示灯 枚举：1.红灯 2.黄灯 3.绿灯 其它灰灯 retCode:消息码 retMsg:消息内容}
 * @param {any}  val 任意值
 * @param {String}  msg 任意值
 * @param {Boolean}  translate 翻译
 */
export function checkNull(msgList, val, msg, translate = true) {
  if (!val) {
    msgList.push({ light: "1", retCode: "E", retMsg: translate ? $t(msg) : msg });
  }
}

/**
 * 添加消息
 * @param {Object}  msgList 消息列表 { light:指示灯 枚举：1.红灯 2.黄灯 3.绿灯 其它灰灯 retCode:消息码 retMsg:消息内容}
 * @param {String}  msg 任意值
 * @param {Boolean}  translate 翻译
 */
export function addMsg(msgList, msg, translate = true) {
  msgList.push({ light: "1", retCode: "E", retMsg: translate ? $t(msg) : msg });
}




