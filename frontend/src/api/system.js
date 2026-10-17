import request from './request'

export const getRoleList = (params) => request.get('/roles', { params })
export const addRole = (data) => request.post('/roles', data)
export const updateRole = (id, data) => request.put(`/roles/${id}`, data)
export const deleteRole = (id) => request.delete(`/roles/${id}`)
export const assignRoleMenus = (roleId, menuIds) => request.post(`/roles/${roleId}/menus`, { menuIds })

export const getMenuList = (params) => request.get('/menus', { params })
export const addMenu = (data) => request.post('/menus', data)
export const updateMenu = (id, data) => request.put(`/menus/${id}`, data)
export const deleteMenu = (id) => request.delete(`/menus/${id}`)

export const getDictList = (params) => request.get('/dicts', { params })
export const addDict = (data) => request.post('/dicts', data)
export const updateDict = (id, data) => request.put(`/dicts/${id}`, data)
export const deleteDict = (id) => request.delete(`/dicts/${id}`)

export const getLogList = (params) => request.get('/logs', { params })
