import request from "szd-client-web/client/common/utils/request";

// 清单查询
export function listApply(data) {
  return request({ url: "/z000/mdm/mtl/list", method: "post", data: data });
}

// 同步SAP物料主数据
export function syncMtl(data) {
  return request({ url: "/z000/mdm/mtl/sync", method: "post", data: data });
}
