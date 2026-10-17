import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { useState, useEffect } from 'react'
import MainLayout from './layouts/MainLayout'
import Login from './pages/Login'
import Dashboard from './pages/Dashboard'
import StudentList from './pages/student/StudentList'
import StudentAdd from './pages/student/StudentAdd'
import UserList from './pages/user/UserList'
import RoleList from './pages/system/RoleList'
import MenuList from './pages/system/MenuList'
import DictList from './pages/system/DictList'
import LogList from './pages/system/LogList'
import './App.css'

function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(false)

  useEffect(() => {
    const token = localStorage.getItem('token')
    setIsAuthenticated(!!token)
  }, [])

  const handleLogin = (token) => {
    localStorage.setItem('token', token)
    setIsAuthenticated(true)
  }

  const handleLogout = () => {
    localStorage.removeItem('token')
    setIsAuthenticated(false)
  }

  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<Login onLogin={handleLogin} />} />
        <Route
          path="/"
          element={
            isAuthenticated ? (
              <MainLayout onLogout={handleLogout} />
            ) : (
              <Navigate to="/login" replace />
            )
          }
        >
          <Route index element={<Dashboard />} />
          <Route path="students" element={<StudentList />} />
          <Route path="students/add" element={<StudentAdd />} />
          <Route path="students/edit/:id" element={<StudentAdd />} />
          <Route path="users" element={<UserList />} />
          <Route path="system/roles" element={<RoleList />} />
          <Route path="system/menus" element={<MenuList />} />
          <Route path="system/dicts" element={<DictList />} />
          <Route path="system/logs" element={<LogList />} />
        </Route>
      </Routes>
    </BrowserRouter>
  )
}

export default App
