import request from "szd-client-app/client/common/utils/request";

// 清单
export function listApply(data) {
  return request({ url: "/z000/pp/orderReport/list", method: "post", data: data });
}

// 查询
export function getApply(query) {
  return request({ url: "/z000/pp/orderReport/get", method: "get", params: query });
}

// 保存
export function saveApply(data, wfEvt) {
  return request({ url: "/z000/pp/orderReport/save", method: "post", data: { data: data, wfEvt: wfEvt } });
}

// 检查
export function checkApply(data, wfEvt) {
  return request({ url: "/z000/pp/orderReport/check", method: "post", data: { data: data, wfEvt: wfEvt } });
}

// 同步SAP
export function syncSapApply(scddbgId) {
  return request({ url: "/z000/pp/orderReport/syncSap/" + scddbgId, method: "get" });
}

// 冲销报工
export function reverseApply(scddbgId) {
  return request({ url: "/z000/pp/orderReport/reverse/" + scddbgId, method: "get" });
}

// 校验扫描结果（订单号+工序编号）
export function validateScan(orderNo, operNo) {
  return request({ url: "/z000/pp/orderReport/validateScan", method: "get", params: { orderNo, operNo } });
}
