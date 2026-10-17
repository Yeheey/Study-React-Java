package com.student.manager.controller;

import com.student.manager.common.Result;
import com.student.manager.dto.LoginRequest;
import com.student.manager.dto.LoginResponse;
import com.student.manager.entity.SysUser;
import com.student.manager.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Tag(name = "认证管理")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "登录")
    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return Result.ok(authService.login(request));
    }

    @Operation(summary = "获取当前用户信息")
    @GetMapping("/info")
    public Result<SysUser> getUserInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return Result.ok(authService.getUserInfo(auth.getName()));
    }

    @Operation(summary = "退出登录")
    @PostMapping("/logout")
    public Result<Void> logout() {
        return Result.ok();
    }
}
