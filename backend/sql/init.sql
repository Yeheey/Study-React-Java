CREATE DATABASE IF NOT EXISTS student_manager DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE student_manager;

-- 用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(200) NOT NULL COMMENT '密码',
    nickname VARCHAR(50) COMMENT '昵称',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    avatar VARCHAR(255) COMMENT '头像',
    status TINYINT DEFAULT 1 COMMENT '状态 0:禁用 1:启用',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除 0:未删除 1:已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '系统用户表';

-- 角色表
CREATE TABLE IF NOT EXISTS sys_role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL COMMENT '角色名称',
    role_key VARCHAR(50) NOT NULL UNIQUE COMMENT '角色标识',
    sort INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态',
    remark VARCHAR(255) COMMENT '备注',
    deleted TINYINT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '角色表';

-- 菜单表
CREATE TABLE IF NOT EXISTS sys_menu (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    parent_id BIGINT DEFAULT 0 COMMENT '父菜单ID',
    menu_name VARCHAR(50) NOT NULL COMMENT '菜单名称',
    menu_type TINYINT COMMENT '类型 1:目录 2:菜单 3:按钮',
    icon VARCHAR(50) COMMENT '图标',
    path VARCHAR(200) COMMENT '路由路径',
    perms VARCHAR(100) COMMENT '权限标识',
    sort INT DEFAULT 0 COMMENT '排序',
    visible TINYINT DEFAULT 1 COMMENT '是否可见',
    deleted TINYINT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '菜单表';

-- 用户角色关联表
CREATE TABLE IF NOT EXISTS sys_user_role (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id)
) COMMENT '用户角色关联表';

-- 角色菜单关联表
CREATE TABLE IF NOT EXISTS sys_role_menu (
    role_id BIGINT NOT NULL,
    menu_id BIGINT NOT NULL,
    PRIMARY KEY (role_id, menu_id)
) COMMENT '角色菜单关联表';

-- 学生表
CREATE TABLE IF NOT EXISTS student (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_no VARCHAR(50) NOT NULL UNIQUE COMMENT '学号',
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    gender VARCHAR(10) COMMENT '性别',
    age INT COMMENT '年龄',
    class_name VARCHAR(50) COMMENT '班级',
    major VARCHAR(100) COMMENT '专业',
    phone VARCHAR(20) COMMENT '手机号',
    email VARCHAR(100) COMMENT '邮箱',
    address VARCHAR(255) COMMENT '地址',
    avatar VARCHAR(255) COMMENT '头像',
    status VARCHAR(20) DEFAULT '在读' COMMENT '状态',
    deleted TINYINT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '学生表';

-- 字典表
CREATE TABLE IF NOT EXISTS sys_dict (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    dict_name VARCHAR(100) NOT NULL COMMENT '字典名称',
    dict_type VARCHAR(100) NOT NULL COMMENT '字典类型',
    dict_value VARCHAR(200) NOT NULL COMMENT '字典值',
    sort INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态',
    remark VARCHAR(255) COMMENT '备注',
    deleted TINYINT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '字典表';

-- 操作日志表
CREATE TABLE IF NOT EXISTS sys_operation_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    operator VARCHAR(50) COMMENT '操作人',
    module VARCHAR(50) COMMENT '操作模块',
    type VARCHAR(50) COMMENT '操作类型',
    method VARCHAR(255) COMMENT '请求方法',
    url VARCHAR(255) COMMENT '请求URL',
    ip VARCHAR(50) COMMENT 'IP地址',
    status TINYINT COMMENT '状态 1:成功 0:失败',
    cost_time BIGINT COMMENT '耗时(ms)',
    error_msg TEXT COMMENT '错误信息',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) COMMENT '操作日志表';

-- 初始化管理员账号 (密码: admin123)
INSERT INTO sys_user (username, password, nickname, status) VALUES
('admin', '$2a$10$VQEDHjZE9k/MoEfiGqLOeOaEFrFfd7wA.oLGBVFeQA1ykNGtGPT5G', '超级管理员', 1);

-- 初始化角色
INSERT INTO sys_role (role_name, role_key, sort, status) VALUES
('超级管理员', 'admin', 1, 1),
('普通用户', 'user', 2, 1);

-- 初始化菜单
INSERT INTO sys_menu (parent_id, menu_name, menu_type, icon, path, perms, sort) VALUES
(0, '仪表盘', 2, 'DashboardOutlined', '/', NULL, 1),
(0, '学生管理', 2, 'TeamOutlined', '/students', NULL, 2),
(0, '用户管理', 2, 'UserOutlined', '/users', NULL, 3),
(0, '系统管理', 1, 'SettingOutlined', '/system', NULL, 4),
(4, '角色管理', 2, 'SafetyOutlined', '/system/roles', NULL, 1),
(4, '菜单管理', 2, 'MenuOutlined', '/system/menus', NULL, 2),
(4, '字典管理', 2, 'BookOutlined', '/system/dicts', NULL, 3),
(4, '操作日志', 2, 'FileTextOutlined', '/system/logs', NULL, 4);

-- 管理员关联角色
INSERT INTO sys_user_role (user_id, role_id) VALUES (1, 1);

-- 管理员角色关联所有菜单
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7), (1, 8);
