import request from "szd-client-web/client/common/utils/request";

// 查询列表
export function listPayFlow(data) {
  return request({ url: "/z000/sd/payFlowCash/list", method: "post", data });
}

// 删除
export function delPayFlow(params) {
  return request({ url: "/z000/sd/payFlowCash/delete", method: "delete", params });
}

// 更新客户
export function updateCust(data) {
  return request({ url: "/z000/sd/payFlowCash/updateCust", method: "post", data });
}

// 查询详情
export function getInfo(params) {
  return request({ url: "/z000/sd/payFlowCash/get", method: "get", params });
}

// 新增
export function save(data) {
  return request({ url: "/z000/sd/payFlowCash/save", method: "post", data });
}

// 修改
export function update(data) {
  return request({ url: "/z000/sd/payFlowCash/edit", method: "post", data });
}
