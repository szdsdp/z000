import request from "szd-client-web/client/common/utils/request";

// 清单
export function listApply(data) {
  return request({
    url: "/z000/mm/checkPlan/list",
    method: "post",
    data: data
  });
}

// 查询
export function getApply(query) {
  return request({
    url: "/z000/mm/checkPlan/get",
    method: "get",
    params: query
  });
}

// 保存
export function saveApply(data, wfEvt) {
  let json = {
    data: data,
    wfEvt: wfEvt
  };
  return request({
    url: "/z000/mm/checkPlan/save",
    method: "post",
    data: json
  });
}

// 检查
export function checkApply(data, wfEvt) {
  let json = {
    data: data,
    wfEvt: wfEvt
  };
  return request({
    url: "/z000/mm/checkPlan/check",
    method: "post",
    data: json
  });
}

// 同步SAP
export function syncSapApply(planId) {
  return request({
    url: "/z000/mm/checkPlan/syncSap/" + planId,
    method: "get"
  });
}

// 冲销
export function reverseApply(planId, budat) {
  return request({
    url: "/z000/mm/checkPlan/reverse",
    method: "post",
    data: { planId: planId, budat: budat }
  });
}

// 从库存查询拉取创建盘点单
export function createFromStock(data) {
  let json = {
    data: data,
    wfEvt: {}
  };
  return request({
    url: "/z000/mm/checkPlan/createFromStock",
    method: "post",
    data: json
  });
}

// 库存查询（复用）
export function queryStock(data) {
  return request({
    url: "/z000/mm/mtlStock/list",
    method: "post",
    data: data
  });
}

// 下发盘点任务
export function distributeApply(data) {
  return request({
    url: "/z000/mm/checkPlan/distribute",
    method: "post",
    data: data
  });
}
