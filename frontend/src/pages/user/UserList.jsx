import { Table, Button, Space, Input, Form, Modal, message, Popconfirm, Card, Tag } from 'antd'
import { PlusOutlined, EditOutlined, DeleteOutlined, KeyOutlined } from '@ant-design/icons'
import { useEffect, useState } from 'react'
import { getUserList, addUser, updateUser, deleteUser, resetPassword } from '../../api/user'

export default function UserList() {
  const [data, setData] = useState([])
  const [loading, setLoading] = useState(false)
  const [pagination, setPagination] = useState({ current: 1, pageSize: 10, total: 0 })
  const [modalOpen, setModalOpen] = useState(false)
  const [editingUser, setEditingUser] = useState(null)
  const [form] = Form.useForm()
  const [searchForm] = Form.useForm()

  const columns = [
    { title: '用户名', dataIndex: 'username', key: 'username' },
    { title: '昵称', dataIndex: 'nickname', key: 'nickname' },
    { title: '邮箱', dataIndex: 'email', key: 'email' },
    { title: '状态', dataIndex: 'status', key: 'status', render: (v) => <Tag color={v === 1 ? 'green' : 'red'}>{v === 1 ? '启用' : '禁用'}</Tag> },
    {
      title: '操作', key: 'action', render: (_, record) => (
        <Space>
          <Button type="link" icon={<EditOutlined />} onClick={() => handleEdit(record)}>编辑</Button>
          <Popconfirm title="确定重置密码？" onConfirm={() => handleResetPwd(record.id)}>
            <Button type="link" icon={<KeyOutlined />}>重置密码</Button>
          </Popconfirm>
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
      const res = await getUserList({ ...values, page, pageSize })
      setData(res.records || [])
      setPagination({ current: res.current, pageSize: res.size, total: res.total })
    } catch { /* handled */ } finally {
      setLoading(false)
    }
  }

  useEffect(() => { fetchData() }, [])

  const handleEdit = (record) => {
    setEditingUser(record)
    form.setFieldsValue(record)
    setModalOpen(true)
  }

  const handleAdd = () => {
    setEditingUser(null)
    form.resetFields()
    setModalOpen(true)
  }

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields()
      if (editingUser) {
        await updateUser(editingUser.id, values)
        message.success('修改成功')
      } else {
        await addUser(values)
        message.success('新增成功')
      }
      setModalOpen(false)
      fetchData(pagination.current, pagination.pageSize)
    } catch { /* handled */ }
  }

  const handleDelete = async (id) => {
    try {
      await deleteUser(id)
      message.success('删除成功')
      fetchData(pagination.current, pagination.pageSize)
    } catch { /* handled */ }
  }

  const handleResetPwd = async (id) => {
    try {
      await resetPassword(id)
      message.success('密码已重置为 123456')
    } catch { /* handled */ }
  }

  return (
    <Card>
      <Form form={searchForm} layout="inline" style={{ marginBottom: 16 }} onFinish={() => fetchData()}>
        <Form.Item name="keyword">
          <Input placeholder="用户名/昵称" allowClear />
        </Form.Item>
        <Form.Item>
          <Button type="primary" htmlType="submit">查询</Button>
        </Form.Item>
      </Form>
      <Button type="primary" icon={<PlusOutlined />} style={{ marginBottom: 16 }} onClick={handleAdd}>新增</Button>
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
      <Modal
        title={editingUser ? '编辑用户' : '新增用户'}
        open={modalOpen}
        onOk={handleSubmit}
        onCancel={() => setModalOpen(false)}
      >
        <Form form={form} layout="vertical">
          <Form.Item name="username" label="用户名" rules={[{ required: true }]}>
            <Input disabled={!!editingUser} />
          </Form.Item>
          {!editingUser && (
            <Form.Item name="password" label="密码" rules={[{ required: true }]}>
              <Input.Password />
            </Form.Item>
          )}
          <Form.Item name="nickname" label="昵称">
            <Input />
          </Form.Item>
          <Form.Item name="email" label="邮箱">
            <Input />
          </Form.Item>
          <Form.Item name="status" label="状态" initialValue={1}>
            <Select>
              <Select.Option value={1}>启用</Select.Option>
              <Select.Option value={0}>禁用</Select.Option>
            </Select>
          </Form.Item>
        </Form>
      </Modal>
    </Card>
  )
}
