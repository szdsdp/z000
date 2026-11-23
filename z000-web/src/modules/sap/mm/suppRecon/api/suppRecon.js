import request from "szd-client-web/client/common/utils/request";

// 供应商对账查询
export function querySuppRecon(data) {
  return request({
    url: "/z000/mm/suppRecon/list",
    method: "post",
    data: data
  });
}
