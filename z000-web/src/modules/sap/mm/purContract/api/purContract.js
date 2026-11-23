import request from "szd-client-web/client/common/utils/request";

// 清单
export function listContract(data) {
  return request({
    url: "/z000/mm/purContract/list",
    method: "post",
    data: data
  });
}

// 查询
export function getContract(query) {
  return request({
    url: "/z000/mm/purContract/get",
    method: "get",
    params: query
  });
}

// 保存
export function saveContract(data, wfEvt) {
  let json = {
    data: data,
    wfEvt: wfEvt
  };
  return request({
    url: "/z000/mm/purContract/save",
    method: "post",
    data: json
  });
}

// 检查
export function checkContract(data, wfEvt) {
  let json = {
    data: data,
    wfEvt: wfEvt
  };
  return request({
    url: "/z000/mm/purContract/check",
    method: "post",
    data: json
  });
}

// 同步SAP
export function syncSapContract(contractId) {
  return request({
    url: "/z000/mm/purContract/syncSap/" + contractId,
    method: "get"
  });
}
