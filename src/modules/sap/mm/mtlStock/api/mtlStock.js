import request from "szd-client-web/client/common/utils/request";

// 物料库存查询（从数据库）
export function queryStock(data) {
  return request({ url: "/z000/mm/mtlStock/list", method: "post", data: data });
}

// 同步库存（调SAP并存库）
export function syncStock(data) {
  return request({ url: "/z000/mm/mtlStock/sync", method: "post", data: data });
}
