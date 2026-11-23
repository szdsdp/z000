import request from "szd-client-web/client/common/utils/request";

// 清单
export function listApply(data) {
  return request({ url: "/z000/mm/checkTask/list", method: "post", data: data });
}

// 查询
export function getApply(query) {
  return request({ url: "/z000/mm/checkTask/get", method: "get", params: query });
}

// 保存
export function saveApply(data, wfEvt) {
  return request({ url: "/z000/mm/checkTask/save", method: "post", data: { data: data, wfEvt: wfEvt } });
}

// 检查
export function checkApply(data, wfEvt) {
  return request({ url: "/z000/mm/checkTask/check", method: "post", data: { data: data, wfEvt: wfEvt } });
}

// 更新行项目（仅传变化行）
export function updateItems(items) {
  return request({ url: "/z000/mm/checkTask/updateItems", method: "post", data: items });
}
