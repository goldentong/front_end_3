import request from '@/utils/request'

// login
export function login(data) {
  return request({
    url: '/user/login',
    method: 'post',
    data: data
  })
}

// search
export function getPageList(params) {
  return request({
    url: '/user',
    method: 'get',
    params: params
  })
}

// add.edit personla info
export function saveOrUpdateData(data) {
  return request({
    url: '/user',
    method: 'post',
    data: data
  })
}

// delete personal info
export function deleteData(id) {
  return request({
    url: '/user/' + id,
    method: 'delete'
  })
}
