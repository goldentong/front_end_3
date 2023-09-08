import request from '@/utils/request'

// search profile
export function getPageList(params) {
  return request({
    url: '/document',
    method: 'get',
    params: params
  })
}

// add profile
export function saveOrUpdateData(data) {
  return request({
    url: '/document',
    method: 'post',
    data: data
  })
}

// delete profile
export function deleteData(id) {
  return request({
    url: '/document/' + id,
    method: 'delete'
  })
}
