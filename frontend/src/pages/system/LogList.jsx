import { Table, Card, Form, Input, DatePicker, Button } from 'antd'
import { useEffect, useState } from 'react'
import { getLogList } from '../../api/system'
import { SearchOutlined } from '@ant-design/icons'

export default function LogList() {
  const [data, setData] = useState([])
  const [loading, setLoading] = useState(false)
  const [pagination, setPagination] = useState({ current: 1, pageSize: 10, total: 0 })
  const [searchForm] = Form.useForm()

  const columns = [
    { title: '操作人', dataIndex: 'operator', key: 'operator' },
    { title: '操作模块', dataIndex: 'module', key: 'module' },
    { title: '操作类型', dataIndex: 'type', key: 'type' },
    { title: '请求方法', dataIndex: 'method', key: 'method' },
    { title: '请求URL', dataIndex: 'url', key: 'url' },
    { title: 'IP地址', dataIndex: 'ip', key: 'ip' },
    { title: '状态', dataIndex: 'status', key: 'status', render: (v) => (v === 1 ? '成功' : '失败') },
    { title: '操作时间', dataIndex: 'createTime', key: 'createTime' },
  ]

  const fetchData = async (page = 1, pageSize = 10) => {
    setLoading(true)
    try {
      const values = searchForm.getFieldsValue()
      const res = await getLogList({ ...values, page, pageSize })
      setData(res.records || [])
      setPagination({ current: res.current, pageSize: res.size, total: res.total })
    } catch { /* handled */ } finally {
      setLoading(false)
    }
  }

  useEffect(() => { fetchData() }, [])

  return (
    <Card>
      <Form form={searchForm} layout="inline" style={{ marginBottom: 16 }} onFinish={() => fetchData()}>
        <Form.Item name="keyword">
          <Input placeholder="操作人/模块" prefix={<SearchOutlined />} allowClear />
        </Form.Item>
        <Form.Item>
          <Button type="primary" htmlType="submit">查询</Button>
        </Form.Item>
      </Form>
      <Table
        columns={columns}
        dataSource={data}
        rowKey="id"
        loading={loading}
        pagination={{ ...pagination, showSizeChanger: true, showTotal: (total) => `共 ${total} 条`, onChange: (p, ps) => fetchData(p, ps) }}
      />
    </Card>
  )
}
