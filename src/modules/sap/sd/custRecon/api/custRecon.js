import request from "szd-client-web/client/common/utils/request";

// 客商对账查询
export function queryCustRecon(data) {
  return request({
    url: "/z000/sd/custRecon/list",
    method: "post",
    data: data
  });
}
