import request from "szd-client-app/client/common/utils/request";

export function listApply(data) { return request({ url: "/z000/sd/saleInv/list", method: "post", data: data }); }
export function getApply(query) { return request({ url: "/z000/sd/saleInv/get", method: "get", params: query }); }
export function saveApply(data, wfEvt) { return request({ url: "/z000/sd/saleInv/save", method: "post", data: { data: data, wfEvt: wfEvt } }); }
export function checkApply(data, wfEvt) { return request({ url: "/z000/sd/saleInv/check", method: "post", data: { data: data, wfEvt: wfEvt } }); }
export function issueInvoiceApply(invId) { return request({ url: "/z000/sd/saleInv/issueInvoice/" + invId, method: "get" }); }
export function cancelInvoiceApply(invId) { return request({ url: "/z000/sd/saleInv/cancelInvoice/" + invId, method: "get" }); }
