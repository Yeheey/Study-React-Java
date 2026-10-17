package com.student.manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.student.manager.common.BusinessException;
import com.student.manager.dto.LoginRequest;
import com.student.manager.dto.LoginResponse;
import com.student.manager.entity.SysUser;
import com.student.manager.mapper.SysUserMapper;
import com.student.manager.security.JwtTokenProvider;
import com.student.manager.security.SecurityUser;
import com.student.manager.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        String token = jwtTokenProvider.generateToken(securityUser);

        SysUser user = getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, request.getUsername()));

        return LoginResponse.builder()
                .token(token)
                .username(securityUser.getUsername())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .permissions(securityUser.getPermissions())
                .build();
    }

    @Override
    public SysUser getUserInfo(String username) {
        SysUser user = getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username));
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        user.setPassword(null);
        return user;
    }
}
