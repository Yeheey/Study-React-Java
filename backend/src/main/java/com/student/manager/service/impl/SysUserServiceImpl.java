package com.student.manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.student.manager.common.BusinessException;
import com.student.manager.dto.UserCreateRequest;
import com.student.manager.dto.UserUpdateRequest;
import com.student.manager.entity.SysUser;
import com.student.manager.mapper.SysUserMapper;
import com.student.manager.mapper.SysUserRoleMapper;
import com.student.manager.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private final PasswordEncoder passwordEncoder;
    private final SysUserRoleMapper userRoleMapper;

    @Override
    public IPage<SysUser> pageList(String keyword, int page, int pageSize) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(SysUser::getUsername, keyword).or().like(SysUser::getNickname, keyword);
        }
        wrapper.orderByDesc(SysUser::getCreateTime);
        IPage<SysUser> result = page(new Page<>(page, pageSize), wrapper);
        result.getRecords().forEach(u -> u.setPassword(null));
        return result;
    }

    @Override
    @Transactional
    public void createUser(UserCreateRequest request) {
        long count = count(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, request.getUsername()));
        if (count > 0) {
            throw new BusinessException("用户名已存在");
        }

        SysUser user = new SysUser();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNickname(request.getNickname());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setStatus(request.getStatus() != null ? request.getStatus() : 1);
        save(user);
    }

    @Override
    public void updateUser(Long id, UserUpdateRequest request) {
        SysUser user = getById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        user.setNickname(request.getNickname());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        if (request.getStatus() != null) {
            user.setStatus(request.getStatus());
        }
        updateById(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        removeById(id);
        userRoleMapper.deleteByUserId(id);
    }

    @Override
    public void resetPassword(Long id) {
        SysUser user = getById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        user.setPassword(passwordEncoder.encode("123456"));
        updateById(user);
    }

    @Override
    @Transactional
    public void assignRoles(Long userId, java.util.List<Long> roleIds) {
        userRoleMapper.deleteByUserId(userId);
        if (roleIds != null) {
            for (Long roleId : roleIds) {
                userRoleMapper.insert(userId, roleId);
            }
        }
    }
}
