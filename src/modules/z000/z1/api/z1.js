import request from 'szd-client-web/client/common/utils/request'

// 清单
export function list(data) {
  return request({
    url: '/z000/z1/list',
    method: 'post',
    data: data
  })
}

// 查询
export function get(query) {
  return request({
    url: '/z000/z1/get',
    method: 'get',
    params: query
  })
}

// 保存
export function save(data, wfEvt) {
  let json = {
    data: data,
    wfEvt: wfEvt
  }
  return request({
    url: '/z000/z1/save',
    method: 'post',
    data: json
  })
}

// 检查
export function check(data, wfEvt) {
  let json = {
    data: data,
    wfEvt: wfEvt
  }
  return request({
    url: '/z000/z1/check',
    method: 'post',
    data: json
  })
}
