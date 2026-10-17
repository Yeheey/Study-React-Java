import request from './request'

export const getUserList = (params) => request.get('/users', { params })
export const getUser = (id) => request.get(`/users/${id}`)
export const addUser = (data) => request.post('/users', data)
export const updateUser = (id, data) => request.put(`/users/${id}`, data)
export const deleteUser = (id) => request.delete(`/users/${id}`)
export const resetPassword = (id) => request.post(`/users/${id}/reset-password`)
