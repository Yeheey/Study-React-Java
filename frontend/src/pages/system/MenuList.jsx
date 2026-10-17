import { Table, Button, Space, Input, Form, Modal, message, Popconfirm, Card, TreeSelect } from 'antd'
import { PlusOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons'
import { useEffect, useState } from 'react'
import { getMenuList, addMenu, updateMenu, deleteMenu } from '../../api/system'

export default function MenuList() {
  const [data, setData] = useState([])
  const [loading, setLoading] = useState(false)
  const [modalOpen, setModalOpen] = useState(false)
  const [editingMenu, setEditingMenu] = useState(null)
  const [form] = Form.useForm()
  const [allMenus, setAllMenus] = useState([])

  const columns = [
    { title: '菜单名称', dataIndex: 'menuName', key: 'menuName' },
    { title: '图标', dataIndex: 'icon', key: 'icon' },
    { title: '路径', dataIndex: 'path', key: 'path' },
    { title: '权限标识', dataIndex: 'perms', key: 'perms' },
    { title: '排序', dataIndex: 'sort', key: 'sort' },
    { title: '类型', dataIndex: 'menuType', key: 'menuType', render: (v) => (v === 1 ? '目录' : v === 2 ? '菜单' : '按钮') },
    {
      title: '操作', key: 'action', render: (_, record) => (
        <Space>
          <Button type="link" icon={<EditOutlined />} onClick={() => handleEdit(record)}>编辑</Button>
          <Popconfirm title="确定删除？" onConfirm={() => handleDelete(record.id)}>
            <Button type="link" danger icon={<DeleteOutlined />}>删除</Button>
          </Popconfirm>
        </Space>
      ),
    },
  ]

  const fetchData = async () => {
    setLoading(true)
    try {
      const res = await getMenuList({})
      setData(res.records || res || [])
      setAllMenus(res.records || res || [])
    } catch { /* handled */ } finally {
      setLoading(false)
    }
  }

  useEffect(() => { fetchData() }, [])

  const buildTree = (list) => list.map((item) => ({
    title: item.menuName,
    value: item.id,
    children: item.children ? buildTree(item.children) : [],
  }))

  const handleEdit = (record) => {
    setEditingMenu(record)
    form.setFieldsValue(record)
    setModalOpen(true)
  }

  const handleAdd = () => {
    setEditingMenu(null)
    form.resetFields()
    setModalOpen(true)
  }

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields()
      if (editingMenu) {
        await updateMenu(editingMenu.id, values)
        message.success('修改成功')
      } else {
        await addMenu(values)
        message.success('新增成功')
      }
      setModalOpen(false)
      fetchData()
    } catch { /* handled */ }
  }

  const handleDelete = async (id) => {
    try {
      await deleteMenu(id)
      message.success('删除成功')
      fetchData()
    } catch { /* handled */ }
  }

  return (
    <Card>
      <Button type="primary" icon={<PlusOutlined />} style={{ marginBottom: 16 }} onClick={handleAdd}>新增</Button>
      <Table columns={columns} dataSource={data} rowKey="id" loading={loading} pagination={false} />
      <Modal title={editingMenu ? '编辑菜单' : '新增菜单'} open={modalOpen} onOk={handleSubmit} onCancel={() => setModalOpen(false)}>
        <Form form={form} layout="vertical">
          <Form.Item name="parentId" label="上级菜单" initialValue={0}>
            <TreeSelect treeData={buildTree(allMenus)} placeholder="请选择" allowClear />
          </Form.Item>
          <Form.Item name="menuName" label="菜单名称" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="menuType" label="菜单类型" rules={[{ required: true }]}>
            <TreeSelect>
              <TreeSelect.TreeNode value={1} title="目录" />
              <TreeSelect.TreeNode value={2} title="菜单" />
              <TreeSelect.TreeNode value={3} title="按钮" />
            </TreeSelect>
          </Form.Item>
          <Form.Item name="icon" label="图标">
            <Input />
          </Form.Item>
          <Form.Item name="path" label="路由路径">
            <Input />
          </Form.Item>
          <Form.Item name="perms" label="权限标识">
            <Input />
          </Form.Item>
          <Form.Item name="sort" label="排序" initialValue={0}>
            <Input type="number" />
          </Form.Item>
        </Form>
      </Modal>
    </Card>
  )
}
