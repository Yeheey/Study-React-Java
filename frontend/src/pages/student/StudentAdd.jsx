import { Form, Input, InputNumber, Select, Button, Card, message } from 'antd'
import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { getStudent, addStudent, updateStudent } from '../../api/student'

export default function StudentAdd() {
  const [form] = Form.useForm()
  const navigate = useNavigate()
  const { id } = useParams()
  const [loading, setLoading] = useState(false)
  const isEdit = !!id

  useEffect(() => {
    if (isEdit) {
      getStudent(id).then((data) => form.setFieldsValue(data)).catch(() => {})
    }
  }, [id])

  const onFinish = async (values) => {
    setLoading(true)
    try {
      if (isEdit) {
        await updateStudent(id, values)
        message.success('修改成功')
      } else {
        await addStudent(values)
        message.success('新增成功')
      }
      navigate('/students')
    } catch { /* handled */ } finally {
      setLoading(false)
    }
  }

  return (
    <Card title={isEdit ? '编辑学生' : '新增学生'}>
      <Form form={form} layout="vertical" onFinish={onFinish} style={{ maxWidth: 600 }}>
        <Form.Item name="studentNo" label="学号" rules={[{ required: true, message: '请输入学号' }]}>
          <Input placeholder="请输入学号" />
        </Form.Item>
        <Form.Item name="name" label="姓名" rules={[{ required: true, message: '请输入姓名' }]}>
          <Input placeholder="请输入姓名" />
        </Form.Item>
        <Form.Item name="gender" label="性别" rules={[{ required: true, message: '请选择性别' }]}>
          <Select placeholder="请选择">
            <Select.Option value="男">男</Select.Option>
            <Select.Option value="女">女</Select.Option>
          </Select>
        </Form.Item>
        <Form.Item name="age" label="年龄" rules={[{ required: true, message: '请输入年龄' }]}>
          <InputNumber min={1} max={150} style={{ width: '100%' }} />
        </Form.Item>
        <Form.Item name="className" label="班级" rules={[{ required: true, message: '请输入班级' }]}>
          <Input placeholder="请输入班级" />
        </Form.Item>
        <Form.Item name="major" label="专业" rules={[{ required: true, message: '请输入专业' }]}>
          <Input placeholder="请输入专业" />
        </Form.Item>
        <Form.Item name="phone" label="手机号">
          <Input placeholder="请输入手机号" />
        </Form.Item>
        <Form.Item>
          <Button type="primary" htmlType="submit" loading={loading}>保存</Button>
          <Button style={{ marginLeft: 8 }} onClick={() => navigate('/students')}>取消</Button>
        </Form.Item>
      </Form>
    </Card>
  )
}
