import { Table, Button, Space, Input, Form, Modal, message, Popconfirm, Card, Upload } from 'antd'
import { PlusOutlined, SearchOutlined, EditOutlined, DeleteOutlined, UploadOutlined, DownloadOutlined } from '@ant-design/icons'
import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { getStudentList, deleteStudent, exportStudents, importStudents } from '../../api/student'

export default function StudentList() {
  const [data, setData] = useState([])
  const [loading, setLoading] = useState(false)
  const [pagination, setPagination] = useState({ current: 1, pageSize: 10, total: 0 })
  const [searchForm] = Form.useForm()
  const navigate = useNavigate()

  const columns = [
    { title: '学号', dataIndex: 'studentNo', key: 'studentNo' },
    { title: '姓名', dataIndex: 'name', key: 'name' },
    { title: '性别', dataIndex: 'gender', key: 'gender' },
    { title: '年龄', dataIndex: 'age', key: 'age' },
    { title: '班级', dataIndex: 'className', key: 'className' },
    { title: '专业', dataIndex: 'major', key: 'major' },
    { title: '手机号', dataIndex: 'phone', key: 'phone' },
    {
      title: '操作', key: 'action', render: (_, record) => (
        <Space>
          <Button type="link" icon={<EditOutlined />} onClick={() => navigate(`/students/edit/${record.id}`)}>编辑</Button>
          <Popconfirm title="确定删除？" onConfirm={() => handleDelete(record.id)}>
            <Button type="link" danger icon={<DeleteOutlined />}>删除</Button>
          </Popconfirm>
        </Space>
      ),
    },
  ]

  const fetchData = async (page = 1, pageSize = 10) => {
    setLoading(true)
    try {
      const values = searchForm.getFieldsValue()
      const res = await getStudentList({ ...values, page, pageSize })
      setData(res.records || [])
      setPagination({ current: res.current, pageSize: res.size, total: res.total })
    } catch { /* handled */ } finally {
      setLoading(false)
    }
  }

  useEffect(() => { fetchData() }, [])

  const handleDelete = async (id) => {
    try {
      await deleteStudent(id)
      message.success('删除成功')
      fetchData(pagination.current, pagination.pageSize)
    } catch { /* handled */ }
  }

  const handleExport = async () => {
    try {
      const values = searchForm.getFieldsValue()
      const blob = await exportStudents(values)
      const url = window.URL.createObjectURL(blob)
      const a = document.createElement('a')
      a.href = url
      a.download = '学生列表.xlsx'
      a.click()
      window.URL.revokeObjectURL(url)
    } catch { /* handled */ }
  }

  const handleImport = async (file) => {
    const formData = new FormData()
    formData.append('file', file)
    try {
      await importStudents(formData)
      message.success('导入成功')
      fetchData()
    } catch { /* handled */ }
    return false
  }

  return (
    <Card>
      <Form form={searchForm} layout="inline" style={{ marginBottom: 16 }} onFinish={() => fetchData()}>
        <Form.Item name="keyword">
          <Input placeholder="学号/姓名" prefix={<SearchOutlined />} allowClear />
        </Form.Item>
        <Form.Item>
          <Button type="primary" htmlType="submit">查询</Button>
        </Form.Item>
      </Form>
      <Space style={{ marginBottom: 16 }}>
        <Button type="primary" icon={<PlusOutlined />} onClick={() => navigate('/students/add')}>新增</Button>
        <Button icon={<DownloadOutlined />} onClick={handleExport}>导出</Button>
        <Upload beforeUpload={() => false} showUploadList={false} customRequest={({ file }) => handleImport(file)}>
          <Button icon={<UploadOutlined />}>导入</Button>
        </Upload>
      </Space>
      <Table
        columns={columns}
        dataSource={data}
        rowKey="id"
        loading={loading}
        pagination={{
          ...pagination,
          showSizeChanger: true,
          showTotal: (total) => `共 ${total} 条`,
          onChange: (page, pageSize) => fetchData(page, pageSize),
        }}
      />
    </Card>
  )
}
