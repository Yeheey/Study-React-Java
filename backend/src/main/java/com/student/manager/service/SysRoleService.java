package com.student.manager.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.student.manager.entity.SysRole;

import java.util.List;

public interface SysRoleService extends IService<SysRole> {
    IPage<SysRole> pageList(String keyword, int page, int pageSize);
    void createRole(SysRole role);
    void updateRole(Long id, SysRole role);
    void deleteRole(Long id);
    void assignMenus(Long roleId, List<Long> menuIds);
}
