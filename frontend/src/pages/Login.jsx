import { Form, Input, Button, Card, message } from 'antd'
import { UserOutlined, LockOutlined } from '@ant-design/icons'
import { login } from '../api/auth'
import { useState } from 'react'

export default function Login({ onLogin }) {
  const [loading, setLoading] = useState(false)

  const onFinish = async (values) => {
    setLoading(true)
    try {
      const token = await login(values)
      message.success('登录成功')
      onLogin(token)
    } catch {
      // error handled by interceptor
    } finally {
      setLoading(false)
    }
  }

  return (
    <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '100vh', background: '#f0f2f5' }}>
      <Card title="学生管理平台" style={{ width: 400 }}>
        <Form onFinish={onFinish} size="large">
          <Form.Item name="username" rules={[{ required: true, message: '请输入用户名' }]}>
            <Input prefix={<UserOutlined />} placeholder="用户名" />
          </Form.Item>
          <Form.Item name="password" rules={[{ required: true, message: '请输入密码' }]}>
            <Input.Password prefix={<LockOutlined />} placeholder="密码" />
          </Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit" loading={loading} block>
              登录
            </Button>
          </Form.Item>
        </Form>
      </Card>
    </div>
  )
}
