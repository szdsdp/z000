import request from "szd-client-web/client/common/utils/request";

// 清单
export function listApply(data) {
  return request({ url: "/z000/sd/payRece/list", method: "post", data });
}

// 查询
export function getApply(query) {
  return request({ url: "/z000/sd/payRece/get", method: "get", params: query });
}

// 保存
export function saveApply(data, wfEvt) {
  return request({ url: "/z000/sd/payRece/save", method: "post", data: { data, wfEvt } });
}

// 检查
export function checkApply(data, wfEvt) {
  return request({ url: "/z000/sd/payRece/check", method: "post", data: { data, wfEvt } });
}

// 同步SAP
export function genSapDocApply(bussId) {
  return request({ url: "/z000/sd/payRece/genSapDoc/" + bussId, method: "get" });
}

// 冲销SAP
export function reverseSapDocApply(bussId) {
  return request({ url: "/z000/sd/payRece/reverseSapDoc/" + bussId, method: "get" });
}

// 获取认领初始数据
export function getDataInit(bankDocType, bankDoc) {
  return request({ url: "/z000/sd/payRece/getDataInit/" + bankDocType + "/" + bankDoc, method: "get" });
}

// 生成回款认领单（票据/电汇快捷入口）
export function createPayReceiptDoc(data) {
  return request({ url: "/z000/sd/payRece/createPayReceiptDoc", method: "post", data });
}
