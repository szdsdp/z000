import request from "szd-client-web/client/common/utils/request";

// 清单
export function listApply(data) {
  return request({
    url: "/z000/demo/expCost/list",
    method: "post",
    data: data
  });
}

// 查询
export function getApply(query) {
  return request({
    url: "/z000/demo/expCost/get",
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
    url: "/z000/demo/expCost/save",
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
    url: "/z000/demo/expCost/check",
    method: "post",
    data: json
  });
}
