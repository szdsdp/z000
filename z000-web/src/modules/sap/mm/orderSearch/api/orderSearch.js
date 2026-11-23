import request from "szd-client-web/client/common/utils/request";

// 采购订单抽取查询
export function queryOrderSearch(data) {
  return request({
    url: "/z000/mm/orderSearch/list",
    method: "post",
    data: data
  });
}
