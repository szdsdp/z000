import request from "szd-client-web/client/common/utils/request";

// 清单
export function listApply(data) {
  return request({
    url: "/z000/fi/vch/list",
    method: "post",
    data: data
  });
}

// 查询
export function getApply(query) {
  return request({
    url: "/z000/fi/vch/get",
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
    url: "/z000/fi/vch/save",
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
    url: "/z000/fi/vch/check",
    method: "post",
    data: json
  });
}

// 同步SAP
export function syncSapApply(costId) {
  return request({
    url: "/z000/fi/vch/syncSap/" + costId,
    method: "get"
  });
}


// 预览凭证
export function genVchView(bussId) {
  return request({
    url: "/z000/fi/vch/genVchView/" + bussId,
    method: "get"
  });
}
