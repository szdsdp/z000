import request from "szd-client-web/client/common/utils/request";

// 清单
export function listApply(data) {
  return request({
    url: "/z000/mdm/cust/list",
    method: "post",
    data: data
  });
}

// 查询
export function getApply(query) {
  return request({
    url: "/z000/mdm/cust/get",
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
    url: "/z000/mdm/cust/save",
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
    url: "/z000/mdm/cust/check",
    method: "post",
    data: json
  });
}

// 同步
export function syncSapApply(bussId) {
  return request({
    url: "/z000/mdm/cust/syncSap/" + bussId,
    method: "get"
  });

// 检查
}export function checkSapApply(json) {
  return request({
    url: "/z000/mdm/cust/checkSap/",
    method: "post",
    data: json
  });
}
