import request from "szd-client-web/client/common/utils/request";

// 清单
export function listApply(data) {
  return request({
    url: "/z000/pp/orderReport/list",
    method: "post",
    data: data
  });
}

// 查询
export function getApply(query) {
  return request({
    url: "/z000/pp/orderReport/get",
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
    url: "/z000/pp/orderReport/save",
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
    url: "/z000/pp/orderReport/check",
    method: "post",
    data: json
  });
}

// 同步SAP
export function syncSapApply(scddbgId) {
  return request({
    url: "/z000/pp/orderReport/syncSap/" + scddbgId,
    method: "get"
  });
}

// 冲销报工
export function reverseApply(scddbgId) {
  return request({
    url: "/z000/pp/orderReport/reverse/" + scddbgId,
    method: "get"
  });
}

// 从生产订单抽取工序创建报工单
export function createFromOrderExtract(data) {
  return request({
    url: "/z000/pp/orderReport/createFromOrderExtract",
    method: "post",
    data: { data: data }
  });
}
