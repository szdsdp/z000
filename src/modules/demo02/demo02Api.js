import request from "szd-client-app/client/common/utils/request";

// 我的申请
export function listApply(data) {
  return request({
    url: "/core/wf/buss/base/apply/list",
    method: "post",
    data: data
  });
}
