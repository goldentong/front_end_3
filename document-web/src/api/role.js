import request from '@/utils/request'

//  get all role .noneed
export function getAllRoles() {
  return request({
    url: '/role/all',
    method: 'get'
  })
}

// get role ,no need
export function getPageList(params) {
  return request({
    url: '/role',
    method: 'get',
    params: params
  })
}

// add.edit role, no need
export function saveOrUpdateData(data) {
  return request({
    url: '/role',
    method: 'post',
    data: data
  })
}

// delete role, no need
export function deleteData(id) {
  return request({
    url: '/role/' + id,
    method: 'delete'
  })
}
