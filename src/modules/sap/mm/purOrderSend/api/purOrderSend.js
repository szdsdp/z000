import request from "szd-client-web/client/common/utils/request";

// 清单查询
export function listSend(data) {
  return request({
    url: "/z000/mm/purOrderSend/list",
    method: "post",
    data: data
  });
}
