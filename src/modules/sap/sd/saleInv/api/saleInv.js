import request from "szd-client-web/client/common/utils/request";

export function listApply(data) {
  return request({
    url: "/z000/sd/saleInv/list",
    method: "post",
    data: data
  });
}

export function getApply(query) {
  return request({
    url: "/z000/sd/saleInv/get",
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
    url: "/z000/sd/saleInv/save",
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
    url: "/z000/sd/saleInv/check",
    method: "post",
    data: json
  });
}

export function issueInvoiceApply(invId) {
  return request({
    url: "/z000/sd/saleInv/issueInvoice/" + invId,
    method: "get"
  });
}

export function cancelInvoiceApply(invId) {
  return request({
    url: "/z000/sd/saleInv/cancelInvoice/" + invId,
    method: "get"
  });
}
