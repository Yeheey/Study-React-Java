import { Table, Button, Space, Input, Form, Modal, message, Popconfirm, Card } from 'antd'
import { PlusOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons'
import { useEffect, useState } from 'react'
import { getDictList, addDict, updateDict, deleteDict } from '../../api/system'

export default function DictList() {
  const [data, setData] = useState([])
  const [loading, setLoading] = useState(false)
  const [pagination, setPagination] = useState({ current: 1, pageSize: 10, total: 0 })
  const [modalOpen, setModalOpen] = useState(false)
  const [editingDict, setEditingDict] = useState(null)
  const [form] = Form.useForm()
  const [searchForm] = Form.useForm()

  const columns = [
    { title: '字典名称', dataIndex: 'dictName', key: 'dictName' },
    { title: '字典类型', dataIndex: 'dictType', key: 'dictType' },
    { title: '字典值', dataIndex: 'dictValue', key: 'dictValue' },
    { title: '备注', dataIndex: 'remark', key: 'remark' },
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

  const fetchData = async (page = 1, pageSize = 10) => {
    setLoading(true)
    try {
      const values = searchForm.getFieldsValue()
      const res = await getDictList({ ...values, page, pageSize })
      setData(res.records || [])
      setPagination({ current: res.current, pageSize: res.size, total: res.total })
    } catch { /* handled */ } finally {
      setLoading(false)
    }
  }

  useEffect(() => { fetchData() }, [])

  const handleEdit = (record) => {
    setEditingDict(record)
    form.setFieldsValue(record)
    setModalOpen(true)
  }

  const handleAdd = () => {
    setEditingDict(null)
    form.resetFields()
    setModalOpen(true)
  }

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields()
      if (editingDict) {
        await updateDict(editingDict.id, values)
        message.success('修改成功')
      } else {
        await addDict(values)
        message.success('新增成功')
      }
      setModalOpen(false)
      fetchData(pagination.current, pagination.pageSize)
    } catch { /* handled */ }
  }

  const handleDelete = async (id) => {
    try {
      await deleteDict(id)
      message.success('删除成功')
      fetchData(pagination.current, pagination.pageSize)
    } catch { /* handled */ }
  }

  return (
    <Card>
      <Form form={searchForm} layout="inline" style={{ marginBottom: 16 }} onFinish={() => fetchData()}>
        <Form.Item name="keyword">
          <Input placeholder="字典名称/类型" allowClear />
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
      <Modal title={editingDict ? '编辑字典' : '新增字典'} open={modalOpen} onOk={handleSubmit} onCancel={() => setModalOpen(false)}>
        <Form form={form} layout="vertical">
          <Form.Item name="dictName" label="字典名称" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="dictType" label="字典类型" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="dictValue" label="字典值" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="remark" label="备注">
            <Input.TextArea />
          </Form.Item>
        </Form>
      </Modal>
    </Card>
  )
}
