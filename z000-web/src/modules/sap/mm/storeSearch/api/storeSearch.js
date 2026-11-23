import request from "szd-client-web/client/common/utils/request";

// 物料凭证抽取查询
export function queryStoreSearch(data) {
  return request({
    url: "/z000/mm/storeSearch/list",
    method: "post",
    data: data
  });
}
