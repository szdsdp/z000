import request from "szd-client-web/client/common/utils/request";

// 清单
export function listApply(data) {
  return request({
    url: "/z000/mm/purStore/list",
    method: "post",
    data: data
  });
}

// 查询
export function getApply(query) {
  return request({
    url: "/z000/mm/purStore/get",
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
    url: "/z000/mm/purStore/save",
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
    url: "/z000/mm/purStore/check",
    method: "post",
    data: json
  });
}

// 同步SAP
export function syncSapApply(storeId) {
  return request({
    url: "/z000/mm/purStore/syncSap/" + storeId,
    method: "get"
  });
}

// 冲销
export function reverseApply(storeId, budat) {
  return request({
    url: "/z000/mm/purStore/reverse",
    method: "post",
    data: { storeId: storeId, budat: budat }
  });
}

// 从采购订单创建物料凭证
export function createFromPurOrder(data) {
  let json = {
    data: data,
    wfEvt: {}
  };
  return request({
    url: "/z000/mm/purStore/createFromPurOrder",
    method: "post",
    data: json
  });
}
