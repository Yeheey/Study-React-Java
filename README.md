# 学生管理平台

一个基于 Spring Boot 3.x + React 的前后端分离学生管理系统，包含用户权限、学生管理、字典管理、操作日志等完整功能。

---

## 技术栈

### 前端

| 技术 | 版本 | 用途 |
|------|------|------|
| React | 19.x | UI 框架 |
| Vite | 8.x | 构建工具 |
| Ant Design | 5.x | UI 组件库 |
| React Router | 7.x | 前端路由 |
| Axios | 1.x | HTTP 请求 |

### 后端

| 技术 | 版本 | 用途 |
|------|------|------|
| Java | 21 LTS | 编程语言 |
| Spring Boot | 3.2.5 | 核心框架 |
| Spring Security | 6.x | 安全认证 |
| JWT (jjwt) | 0.12.5 | Token 认证 |
| MyBatis-Plus | 3.5.6 | 持久层框架 |
| MySQL | 8.0 | 关系型数据库 |
| Redis | 7.x | 缓存（可选） |
| Knife4j | 4.4.0 | API 文档（Swagger） |
| EasyExcel | 3.3.4 | Excel 导入导出 |
| Maven | 3.9.x | 项目构建 |
| Lombok | - | 代码简化 |
| Logback | - | 日志框架 |

### 部署

| 技术 | 用途 |
|------|------|
| Docker + Docker Compose | 容器化部署 |
| Nginx | 反向代理 + 静态资源 |

---

## 默认账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 超级管理员 | admin | admin123 |
| MySQL root | root | root（需与你的实际密码一致） |
| Redis | 无密码 | - |

---

## 环境要求

启动前请确保已安装以下软件：

- **JDK 21**（推荐 OpenJDK 21）
- **Maven 3.9+**
- **Node.js 18+**
- **MySQL 8.0**（已启动，端口 3306）
- **Redis 7.x**（可选，不安装也能启动，部分缓存功能不可用）

---

## 启动步骤

### 第一步：初始化数据库

1. 打开 MySQL 客户端（命令行、Navicat、MySQL Workbench 均可）
2. 执行 `backend/sql/init.sql` 脚本

**命令行方式：**
```bash
mysql -u root -p < F:\Code\Study-React-Java\backend\sql\init.sql
```

**MySQL Workbench 方式：**
1. 打开 Workbench，连接到 localhost:3306
2. 点击菜单 File → Open SQL Script
3. 选择 `F:\Code\Study-React-Java\backend\sql\init.sql`
4. 点击闪电图标执行

执行成功后会创建 `student_manager` 数据库及所有表，并插入管理员账号和初始菜单数据。

### 第二步：修改数据库密码（重要）

打开 `backend/src/main/resources/application-dev.yml`，将密码改为你实际的 MySQL root 密码：

```yaml
spring:
  datasource:
    username: root
    password: 你的实际密码    # ← 改成你的 MySQL 密码
```

### 第三步：启动后端

```bash
cd F:\Code\Study-React-Java\backend
mvn spring-boot:run
```

看到以下日志表示启动成功：
```
Started StudentManagerApplication in x.xxx seconds
```

后端地址：http://localhost:8080
API 文档：http://localhost:8080/doc.html

### 第四步：启动前端

**新开一个终端窗口：**
```bash
cd F:\Code\Study-React-Java\frontend
npm install        # 首次需要安装依赖
npm run dev
```

前端地址：http://localhost:3000

### 第五步：登录系统

1. 浏览器打开 http://localhost:3000
2. 用户名：`admin`
3. 密码：`admin123`
4. 点击登录

---

## 项目结构

```
Study-React-Java/
├── frontend/                    # 前端工程
│   ├── src/
│   │   ├── api/                 # API 请求封装
│   │   │   ├── request.js       # Axios 实例 + 拦截器
│   │   │   ├── auth.js          # 认证接口
│   │   │   ├── student.js       # 学生接口
│   │   │   ├── user.js          # 用户接口
│   │   │   └── system.js        # 系统接口（角色/菜单/字典/日志）
│   │   ├── layouts/             # 布局组件
│   │   │   └── MainLayout.jsx   # 主布局（侧边栏+顶栏）
│   │   ├── pages/               # 页面组件
│   │   │   ├── Login.jsx        # 登录页
│   │   │   ├── Dashboard.jsx    # 仪表盘
│   │   │   ├── student/         # 学生管理
│   │   │   ├── user/            # 用户管理
│   │   │   └── system/          # 系统管理
│   │   ├── App.jsx              # 路由配置
│   │   └── main.jsx             # 入口文件
│   ├── vite.config.js           # Vite 配置（含代理）
│   └── package.json
│
├── backend/                     # 后端工程
│   ├── src/main/java/com/student/manager/
│   │   ├── config/              # 配置类
│   │   │   ├── SecurityConfig.java      # Spring Security 配置
│   │   │   ├── MyBatisPlusConfig.java   # MyBatis-Plus 分页插件
│   │   │   ├── RedisConfig.java         # Redis 配置
│   │   │   ── CorsConfig.java          # 跨域配置
│   │   ├── security/            # 安全认证
│   │   │   ├── JwtTokenProvider.java    # JWT 工具类
│   │   │   ├── JwtAuthenticationFilter.java  # JWT 过滤器
│   │   │   ├── SecurityUser.java        # 安全用户对象
│   │   │   └── UserDetailsServiceImpl.java  # 用户详情服务
│   │   ├── common/              # 公共组件
│   │   │   ├── Result.java              # 统一响应
│   │   │   ├── BusinessException.java   # 业务异常
│   │   │   ├── GlobalExceptionHandler.java  # 全局异常处理
│   │   │   ├── Log.java                 # 操作日志注解
│   │   │   ├── LogAspect.java           # 日志切面
│   │   │   └── BaseEntity.java          # 实体基类
│   │   ├── entity/              # 数据库实体
│   │   ├── mapper/              # MyBatis Mapper
│   │   ├── service/             # 业务接口
│   │   ├── service/impl/        # 业务实现
│   │   ├── controller/          # REST 控制器
│   │   └── dto/                 # 数据传输对象
│   ├── src/main/resources/
│   │   ├── application.yml      # 主配置
│   │   ├── application-dev.yml  # 开发环境配置
│   │   ├── application-prod.yml # 生产环境配置
│   │   └── logback-spring.xml   # 日志配置
│   ├── sql/init.sql             # 数据库初始化脚本
│   ├── Dockerfile               # Docker 镜像
│   ├── docker-compose.yml       # Docker 编排
│   ├── nginx.conf               # Nginx 配置
│   └── pom.xml                  # Maven 配置
│
└── README.md                    # 本文件
```

---

## 功能模块

### 基础功能
- 用户登录 / JWT Token 认证
- 用户管理（增删改查、重置密码、分配角色）
- 角色管理（增删改查、分配菜单权限）
- 菜单管理（树形结构、目录/菜单/按钮）
- 字典管理
- 操作日志（AOP 自动记录）

### 学生管理
- 学生列表（分页、条件搜索）
- 新增 / 编辑 / 删除学生
- Excel 导入学生
- Excel 导出学生

### 进阶功能
- Redis 缓存登录态和权限（可选）
- 接口限流
- 统一响应格式
- 统一异常处理
- 多环境配置（dev / prod）
- Docker 容器化部署

---

## Docker 一键部署

如果你安装了 Docker，可以一键启动所有服务：

```bash
cd F:\Code\Study-React-Java\backend

# 先构建前端
cd ../frontend && npm run build && cd ../backend

# 启动所有服务
docker-compose up -d
```

访问 http://localhost 即可使用系统。

---

## 常见问题

### Q: 后端启动报 "Access denied for user 'root'"
A: 修改 `application-dev.yml` 中的 `spring.datasource.password` 为你实际的 MySQL 密码。

### Q: 前端登录报 "网络错误"
A: 后端没有启动。先启动后端，再刷新前端页面重新登录。

### Q: 前端页面空白或 404
A: 检查 Vite 代理配置，确保 `vite.config.js` 中 proxy 指向 `http://localhost:8080`。

### Q: Redis 未安装，后端能启动吗？
A: 可以。Redis 是可选的，不安装也能正常启动，只是缓存功能不可用。

### Q: 如何修改端口？
A: 前端修改 `vite.config.js` 中的 `server.port`；后端修改 `application.yml` 中的 `server.port`。

---

## API 文档

启动后端后访问：http://localhost:8080/doc.html

Knife4j 提供了完整的接口文档和在线调试功能。
