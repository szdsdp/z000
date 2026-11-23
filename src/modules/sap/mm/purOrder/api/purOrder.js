import request from "szd-client-web/client/common/utils/request";

// 清单
export function listApply(data) {
  return request({
    url: "/z000/mm/purOrder/list",
    method: "post",
    data: data
  });
}

// 查询
export function getApply(query) {
  return request({
    url: "/z000/mm/purOrder/get",
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
    url: "/z000/mm/purOrder/save",
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
    url: "/z000/mm/purOrder/check",
    method: "post",
    data: json
  });
}

// 从采购订单清单生成物料凭证
export function createPurStoreFromPurOrderList(purOrderIds) {
  return request({
    url: "/z000/mm/cgrk/createFromPurOrderList",
    method: "post",
    data: purOrderIds
  });
}

// 同步SAP
export function syncSapApply(purOrderId) {
  return request({
    url: "/z000/mm/purOrder/syncSap/" + purOrderId,
    method: "get"
  });
}

