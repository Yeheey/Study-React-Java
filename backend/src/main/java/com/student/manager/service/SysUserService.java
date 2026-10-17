package com.student.manager.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.student.manager.dto.UserCreateRequest;
import com.student.manager.dto.UserUpdateRequest;
import com.student.manager.entity.SysUser;

public interface SysUserService extends IService<SysUser> {
    IPage<SysUser> pageList(String keyword, int page, int pageSize);
    void createUser(UserCreateRequest request);
    void updateUser(Long id, UserUpdateRequest request);
    void deleteUser(Long id);
    void resetPassword(Long id);
    void assignRoles(Long userId, java.util.List<Long> roleIds);
}
