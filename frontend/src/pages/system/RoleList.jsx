import { Table, Button, Space, Input, Form, Modal, message, Popconfirm, Card, Transfer } from 'antd'
import { PlusOutlined, EditOutlined, DeleteOutlined, SettingOutlined } from '@ant-design/icons'
import { useEffect, useState } from 'react'
import { getRoleList, addRole, updateRole, deleteRole, assignRoleMenus, getMenuList } from '../../api/system'

export default function RoleList() {
  const [data, setData] = useState([])
  const [loading, setLoading] = useState(false)
  const [pagination, setPagination] = useState({ current: 1, pageSize: 10, total: 0 })
  const [modalOpen, setModalOpen] = useState(false)
  const [menuModalOpen, setMenuModalOpen] = useState(false)
  const [editingRole, setEditingRole] = useState(null)
  const [form] = Form.useForm()
  const [searchForm] = Form.useForm()
  const [allMenus, setAllMenus] = useState([])
  const [selectedMenuKeys, setSelectedMenuKeys] = useState([])

  const columns = [
    { title: '角色名称', dataIndex: 'roleName', key: 'roleName' },
    { title: '角色标识', dataIndex: 'roleKey', key: 'roleKey' },
    { title: '备注', dataIndex: 'remark', key: 'remark' },
    {
      title: '操作', key: 'action', render: (_, record) => (
        <Space>
          <Button type="link" icon={<EditOutlined />} onClick={() => handleEdit(record)}>编辑</Button>
          <Button type="link" icon={<SettingOutlined />} onClick={() => handleAssignMenu(record)}>分配菜单</Button>
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
      const res = await getRoleList({ ...values, page, pageSize })
      setData(res.records || [])
      setPagination({ current: res.current, pageSize: res.size, total: res.total })
    } catch { /* handled */ } finally {
      setLoading(false)
    }
  }

  useEffect(() => { fetchData() }, [])

  const handleEdit = (record) => {
    setEditingRole(record)
    form.setFieldsValue(record)
    setModalOpen(true)
  }

  const handleAdd = () => {
    setEditingRole(null)
    form.resetFields()
    setModalOpen(true)
  }

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields()
      if (editingRole) {
        await updateRole(editingRole.id, values)
        message.success('修改成功')
      } else {
        await addRole(values)
        message.success('新增成功')
      }
      setModalOpen(false)
      fetchData(pagination.current, pagination.pageSize)
    } catch { /* handled */ }
  }

  const handleDelete = async (id) => {
    try {
      await deleteRole(id)
      message.success('删除成功')
      fetchData(pagination.current, pagination.pageSize)
    } catch { /* handled */ }
  }

  const handleAssignMenu = async (record) => {
    setEditingRole(record)
    try {
      const menus = await getMenuList({})
      setAllMenus((menus.records || menus || []).map((m) => ({ key: m.id, title: m.menuName })))
      setSelectedMenuKeys(record.menuIds || [])
      setMenuModalOpen(true)
    } catch { /* handled */ }
  }

  const handleMenuAssign = async () => {
    try {
      await assignRoleMenus(editingRole.id, selectedMenuKeys)
      message.success('分配成功')
      setMenuModalOpen(false)
    } catch { /* handled */ }
  }

  return (
    <Card>
      <Form form={searchForm} layout="inline" style={{ marginBottom: 16 }} onFinish={() => fetchData()}>
        <Form.Item name="keyword">
          <Input placeholder="角色名称" allowClear />
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
        pagination={{ ...pagination, showSizeChanger: true, showTotal: (total) => `共 ${total} 条`, onChange: (p, ps) => fetchData(p, ps) }}
      />
      <Modal title={editingRole ? '编辑角色' : '新增角色'} open={modalOpen} onOk={handleSubmit} onCancel={() => setModalOpen(false)}>
        <Form form={form} layout="vertical">
          <Form.Item name="roleName" label="角色名称" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="roleKey" label="角色标识" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="remark" label="备注">
            <Input.TextArea />
          </Form.Item>
        </Form>
      </Modal>
      <Modal title="分配菜单" open={menuModalOpen} onOk={handleMenuAssign} onCancel={() => setMenuModalOpen(false)}>
        <Transfer
          dataSource={allMenus}
          targetKeys={selectedMenuKeys}
          onChange={setSelectedMenuKeys}
          render={(item) => item.title}
        />
      </Modal>
    </Card>
  )
}
