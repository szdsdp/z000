import request from "szd-client-web/client/common/utils/request";

export function listApply(data) {
  return request({
    url: "/z000/sd/saleDelivery/list",
    method: "post",
    data: data
  });
}

export function getApply(query) {
  return request({
    url: "/z000/sd/saleDelivery/get",
    method: "get",
    params: query
  });
}

export function saveApply(data, wfEvt) {
  let json = {
    data: data,
    wfEvt: wfEvt
  };
  return request({
    url: "/z000/sd/saleDelivery/save",
    method: "post",
    data: json
  });
}

export function checkApply(data, wfEvt) {
  let json = {
    data: data,
    wfEvt: wfEvt
  };
  return request({
    url: "/z000/sd/saleDelivery/check",
    method: "post",
    data: json
  });
}

export function syncSapApply(deliveryId) {
  return request({
    url: "/z000/sd/saleDelivery/syncSap/" + deliveryId,
    method: "get"
  });
}

export function postApply(data) {
  return request({
    url: "/z000/sd/saleDelivery/post",
    method: "post",
    data: data
  });
}

export function reversePostApply(deliveryId) {
  return request({
    url: "/z000/sd/saleDelivery/reversePost/" + deliveryId,
    method: "get"
  });
}

export function createFromSaleOrder(data) {
  let json = {
    data: data,
    wfEvt: {}
  };
  return request({
    url: "/z000/sd/saleDelivery/createFromSaleOrder",
    method: "post",
    data: json
  });
}
