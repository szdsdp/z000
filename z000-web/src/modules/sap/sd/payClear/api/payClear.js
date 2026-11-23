import request from "szd-client-web/client/common/utils/request";

// 客商未清项抽取查询
export function queryPayClear(data) {
  return request({
    url: "/z000/sd/payClear/list",
    method: "post",
    data: data
  });
}
