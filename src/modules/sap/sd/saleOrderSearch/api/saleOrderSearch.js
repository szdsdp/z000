import request from "szd-client-web/client/common/utils/request";

// 销售订单抽取查询
export function querySaleOrderSearch(data) {
  return request({
    url: "/z000/sd/saleOrderSearch/list",
    method: "post",
    data: data
  });
}
