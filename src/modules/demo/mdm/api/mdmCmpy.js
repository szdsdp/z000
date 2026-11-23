import request from "szd-client-web/client/common/utils/request";

// 查询公司列表
export function listCompany(data) {
  return request({
    url: "/z000/demo/mdmCmpy/list",
    method: "post",
    data: data
  });
}

// 查询公司详细
export function getCompany(cmpy) {
  return request({
    url: "/z000/demo/mdmCmpy/" + cmpy,
    method: "get"
  });
}

// 新增公司
export function addCompany(data) {
  return request({
    url: "/z000/demo/mdmCmpy",
    method: "post",
    data: data
  });
}

// 修改公司
export function updCompany(data) {
  return request({
    url: "/z000/demo/mdmCmpy",
    method: "put",
    data: data
  });
}

// 删除公司
export function delCompany(cmpy) {
  return request({
    url: "/z000/demo/mdmCmpy",
    method: "delete",
    data: cmpy
  });
}
