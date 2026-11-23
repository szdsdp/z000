import request from "szd-client-app/client/common/utils/request";

// 清单
export function listApply(data) {
  return request({ url: "/z000/mm/invCheck/list", method: "post", data: data });
}

// 查询
export function getApply(query) {
  return request({ url: "/z000/mm/invCheck/get", method: "get", params: query });
}

// 保存
export function saveApply(data, wfEvt) {
  return request({ url: "/z000/mm/invCheck/save", method: "post", data: { data: data, wfEvt: wfEvt } });
}

// 检查
export function checkApply(data, wfEvt) {
  return request({ url: "/z000/mm/invCheck/check", method: "post", data: { data: data, wfEvt: wfEvt } });
}

// 同步SAP
export function syncSapApply(invId) {
  return request({ url: "/z000/mm/invCheck/syncSap/" + invId, method: "get" });
}

// 冲销
export function reverseApply(invId) {
  return request({ url: "/z000/mm/invCheck/reverse", method: "post", data: { invId: invId } });
}
