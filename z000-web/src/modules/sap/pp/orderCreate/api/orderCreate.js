import request from "szd-client-web/client/common/utils/request";

// 清单
export function listApply(data) {
  return request({
    url: "/z000/pp/orderCreate/list",
    method: "post",
    data: data
  });
}

// 查询
export function getApply(query) {
  return request({
    url: "/z000/pp/orderCreate/get",
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
    url: "/z000/pp/orderCreate/save",
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
    url: "/z000/pp/orderCreate/check",
    method: "post",
    data: json
  });
}

// 同步SAP
export function syncSapApply(applyId) {
  return request({
    url: "/z000/pp/orderCreate/syncSap/" + applyId,
    method: "get"
  });
}

// 下达生产订单
export function releaseApply(applyId) {
  return request({
    url: "/z000/pp/orderCreate/release/" + applyId,
    method: "get"
  });
}

// 关闭/取消关闭生产订单
export function closeApply(applyId, ztype) {
  return request({
    url: "/z000/pp/orderCreate/close/" + applyId,
    method: "get",
    params: { ztype: ztype }
  });
}
