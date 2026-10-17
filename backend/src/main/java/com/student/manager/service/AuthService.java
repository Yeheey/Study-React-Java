package com.student.manager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.student.manager.dto.LoginRequest;
import com.student.manager.dto.LoginResponse;
import com.student.manager.entity.SysUser;

public interface AuthService extends IService<SysUser> {
    LoginResponse login(LoginRequest request);
    SysUser getUserInfo(String username);
}
