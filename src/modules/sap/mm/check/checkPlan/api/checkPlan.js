import request from "szd-client-app/client/common/utils/request";

// 清单
export function listApply(data) {
  return request({ url: "/z000/mm/checkPlan/list", method: "post", data: data });
}

// 查询
export function getApply(query) {
  return request({ url: "/z000/mm/checkPlan/get", method: "get", params: query });
}

// 保存
export function saveApply(data, wfEvt) {
  return request({ url: "/z000/mm/checkPlan/save", method: "post", data: { data: data, wfEvt: wfEvt } });
}

// 检查
export function checkApply(data, wfEvt) {
  return request({ url: "/z000/mm/checkPlan/check", method: "post", data: { data: data, wfEvt: wfEvt } });
}

// 同步SAP
export function syncSapApply(planId) {
  return request({ url: "/z000/mm/checkPlan/syncSap/" + planId, method: "get" });
}

// 冲销
export function reverseApply(planId, budat) {
  return request({ url: "/z000/mm/checkPlan/reverse", method: "post", data: { planId: planId, budat: budat } });
}

// 下发盘点任务
export function distributeApply(data) {
  return request({ url: "/z000/mm/checkPlan/distribute", method: "post", data: data });
}
