package com.student.manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.student.manager.entity.SysRole;
import com.student.manager.mapper.SysRoleMapper;
import com.student.manager.mapper.SysRoleMenuMapper;
import com.student.manager.service.SysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    private final SysRoleMenuMapper roleMenuMapper;

    @Override
    public IPage<SysRole> pageList(String keyword, int page, int pageSize) {
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(SysRole::getRoleName, keyword);
        }
        wrapper.orderByAsc(SysRole::getSort);
        return page(new Page<>(page, pageSize), wrapper);
    }

    @Override
    public void createRole(SysRole role) {
        save(role);
    }

    @Override
    public void updateRole(Long id, SysRole role) {
        role.setId(id);
        updateById(role);
    }

    @Override
    @Transactional
    public void deleteRole(Long id) {
        removeById(id);
        roleMenuMapper.deleteByRoleId(id);
    }

    @Override
    @Transactional
    public void assignMenus(Long roleId, List<Long> menuIds) {
        roleMenuMapper.deleteByRoleId(roleId);
        if (menuIds != null) {
            for (Long menuId : menuIds) {
                roleMenuMapper.insert(roleId, menuId);
            }
        }
    }
}
