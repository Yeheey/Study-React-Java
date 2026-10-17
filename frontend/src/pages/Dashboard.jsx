import { Card, Row, Col, Statistic } from 'antd'
import { TeamOutlined, UserOutlined, SafetyOutlined, BookOutlined } from '@ant-design/icons'

export default function Dashboard() {
  return (
    <div>
      <h2 style={{ marginBottom: 24 }}>仪表盘</h2>
      <Row gutter={16}>
        <Col span={6}>
          <Card>
            <Statistic title="学生总数" value={0} prefix={<TeamOutlined />} />
          </Card>
        </Col>
        <Col span={6}>
          <Card>
            <Statistic title="用户总数" value={0} prefix={<UserOutlined />} />
          </Card>
        </Col>
        <Col span={6}>
          <Card>
            <Statistic title="角色总数" value={0} prefix={<SafetyOutlined />} />
          </Card>
        </Col>
        <Col span={6}>
          <Card>
            <Statistic title="字典总数" value={0} prefix={<BookOutlined />} />
          </Card>
        </Col>
      </Row>
    </div>
  )
}
