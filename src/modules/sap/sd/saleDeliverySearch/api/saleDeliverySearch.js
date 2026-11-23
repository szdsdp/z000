import request from "szd-client-web/client/common/utils/request";

// 交货单抽取查询
export function querySaleDeliverySearch(data) {
  return request({
    url: "/z000/sd/saleDeliverySearch/list",
    method: "post",
    data: data
  });
}
