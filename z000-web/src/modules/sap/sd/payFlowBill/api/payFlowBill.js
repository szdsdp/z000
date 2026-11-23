import request from "szd-client-web/client/common/utils/request";

// 查询列表
export function listBill(data) {
  return request({ url: "/z000/sd/payFlowBill/list", method: "post", data });
}

// 删除
export function delBill(params) {
  return request({ url: "/z000/sd/payFlowBill/delete", method: "delete", params });
}

// 更新客户
export function updateCust(data) {
  return request({ url: "/z000/sd/payFlowBill/updateCust", method: "post", data });
}

// 查询详情
export function getInfo(params) {
  return request({ url: "/z000/sd/payFlowBill/get", method: "get", params });
}

// 新增
export function save(data) {
  return request({ url: "/z000/sd/payFlowBill/save", method: "post", data });
}

// 修改
export function update(data) {
  return request({ url: "/z000/sd/payFlowBill/edit", method: "post", data });
}
