import { Layout, Menu, Dropdown, Avatar, Space, theme } from 'antd'
import {
  DashboardOutlined,
  TeamOutlined,
  UserOutlined,
  SettingOutlined,
  SafetyOutlined,
  MenuOutlined,
  BookOutlined,
  FileTextOutlined,
  LogoutOutlined,
} from '@ant-design/icons'
import { Outlet, useNavigate, useLocation } from 'react-router-dom'

const { Header, Sider, Content } = Layout

const menuItems = [
  { key: '/', icon: <DashboardOutlined />, label: '仪表盘' },
  { key: '/students', icon: <TeamOutlined />, label: '学生管理' },
  { key: '/users', icon: <UserOutlined />, label: '用户管理' },
  {
    key: 'system',
    icon: <SettingOutlined />,
    label: '系统管理',
    children: [
      { key: '/system/roles', icon: <SafetyOutlined />, label: '角色管理' },
      { key: '/system/menus', icon: <MenuOutlined />, label: '菜单管理' },
      { key: '/system/dicts', icon: <BookOutlined />, label: '字典管理' },
      { key: '/system/logs', icon: <FileTextOutlined />, label: '操作日志' },
    ],
  },
]

export default function MainLayout({ onLogout }) {
  const navigate = useNavigate()
  const location = useLocation()
  const { token: { colorBgContainer } } = theme.useToken()

  const userMenuItems = [
    { key: 'logout', icon: <LogoutOutlined />, label: '退出登录', danger: true },
  ]

  const handleUserMenu = ({ key }) => {
    if (key === 'logout') {
      onLogout()
      navigate('/login')
    }
  }

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Sider theme="dark" collapsible>
        <div className="logo">学生管理平台</div>
        <Menu
          theme="dark"
          mode="inline"
          selectedKeys={[location.pathname]}
          defaultOpenKeys={['system']}
          items={menuItems}
          onClick={({ key }) => navigate(key)}
        />
      </Sider>
      <Layout>
        <Header style={{ padding: '0 24px', background: colorBgContainer, display: 'flex', justifyContent: 'flex-end', alignItems: 'center' }}>
          <Dropdown menu={{ items: userMenuItems, onClick: handleUserMenu }}>
            <Space style={{ cursor: 'pointer' }}>
              <Avatar icon={<UserOutlined />} />
              <span>管理员</span>
            </Space>
          </Dropdown>
        </Header>
        <Content style={{ margin: '24px 16px', padding: 24, background: colorBgContainer, borderRadius: 8, minHeight: 280 }}>
          <Outlet />
        </Content>
      </Layout>
    </Layout>
  )
}
