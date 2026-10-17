package com.student.manager.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.student.manager.entity.SysMenu;

import java.util.List;

public interface SysMenuService extends IService<SysMenu> {
    List<SysMenu> listAll(String keyword);
    List<SysMenu> buildMenuTree(List<SysMenu> menus);
    void createMenu(SysMenu menu);
    void updateMenu(Long id, SysMenu menu);
    void deleteMenu(Long id);
}
