import request from './request'

export const getStudentList = (params) => request.get('/students', { params })
export const getStudent = (id) => request.get(`/students/${id}`)
export const addStudent = (data) => request.post('/students', data)
export const updateStudent = (id, data) => request.put(`/students/${id}`, data)
export const deleteStudent = (id) => request.delete(`/students/${id}`)
export const exportStudents = (params) => request.get('/students/export', { params, responseType: 'blob' })
export const importStudents = (data) => request.post('/students/import', data, {
  headers: { 'Content-Type': 'multipart/form-data' },
})
