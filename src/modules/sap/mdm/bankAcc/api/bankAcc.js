import request from "szd-client-web/client/common/utils/request";

// 查询本方账户列表
export function listBankAcc(data) {
  return request({
    url: "/z000/mdm/bankAcc/list",
    method: "post",
    data: data
  });
}

// 查询本方账户详情
export function getBankAcc(accNo) {
  return request({
    url: "/z000/mdm/bankAcc/get",
    method: "get",
    params: { accNo }
  });
}

// 新增本方账户
export function addBankAcc(data) {
  return request({
    url: "/z000/mdm/bankAcc/save",
    method: "post",
    data: data
  });
}

// 修改本方账户
export function updBankAcc(data) {
  return request({
    url: "/z000/mdm/bankAcc/edit",
    method: "post",
    data: data
  });
}

// 删除本方账户
export function delBankAcc(accNo) {
  return request({
    url: "/z000/mdm/bankAcc/delete",
    method: "delete",
    params: { accNo }
  });
}

// 导入Excel
export function importBankAcc(data) {
  return request({
    url: "/z000/mdm/bankAcc/import",
    method: "post",
    data: data
  });
}
