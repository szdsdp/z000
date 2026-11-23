import request from "szd-client-web/client/common/utils/request";

export function listApply(data) {
  return request({ url: "/z000/pp/orderExtract/list", method: "post", data: data });
}

export function getApply(query) {
  return request({ url: "/z000/pp/orderExtract/get", method: "get", params: query });
}

export function syncApply(data) {
  return request({ url: "/z000/pp/orderExtract/sync", method: "post", data: data });
}
