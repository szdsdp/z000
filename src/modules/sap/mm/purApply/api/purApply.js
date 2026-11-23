import request from "szd-client-web/client/common/utils/request";

// 清单
export function listApply(data) {
  return request({
    url: "/z000/mm/purApply/list",
    method: "post",
    data: data
  });
}

// 查询
export function getApply(query) {
  return request({
    url: "/z000/mm/purApply/get",
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
    url: "/z000/mm/purApply/save",
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
    url: "/z000/mm/purApply/check",
    method: "post",
    data: json
  });
}

// 生成采购订单
export function createPurOrderFromApply(applyId) {
  return request({
    url: "/z000/mm/purApply/createPurOrder/" + applyId,
    method: "post"
  });
}

// 同步SAP
export function syncSapApply(applyId) {
  return request({
    url: "/z000/mm/purApply/syncSap/" + applyId,
    method: "get"
  });
}
