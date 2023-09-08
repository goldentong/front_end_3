import request from '@/utils/request'

// get menu
export function getAllMenus() {
  return request({
    url: '/menu/all',
    method: 'get'
  })
}

// no need for now
export function getMenus() {
  return request({
    url: '/menu',
    method: 'get'
  })
}

// no need for now
export function getPermissionMenus(params) {
  return request({
    url: '/menu/permission',
    method: 'get',
    params: params
  })
}

// no need for now
export function updateData(data) {
  return request({
    url: '/menu',
    method: 'put',
    data: data
  })
}
