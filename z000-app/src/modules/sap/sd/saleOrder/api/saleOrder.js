import request from "szd-client-app/client/common/utils/request";

// 清单
export function listApply(data) {
  return request({ url: "/z000/sd/saleOrder/list", method: "post", data: data });
}

// 查询
export function getApply(query) {
  return request({ url: "/z000/sd/saleOrder/get", method: "get", params: query });
}

// 保存
export function saveApply(data, wfEvt) {
  return request({ url: "/z000/sd/saleOrder/save", method: "post", data: { data: data, wfEvt: wfEvt } });
}

// 检查
export function checkApply(data, wfEvt) {
  return request({ url: "/z000/sd/saleOrder/check", method: "post", data: { data: data, wfEvt: wfEvt } });
}

// 同步SAP
export function syncSapApply(orderId) {
  return request({ url: "/z000/sd/saleOrder/syncSap/" + orderId, method: "get" });
}
