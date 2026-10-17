package com.student.manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.student.manager.entity.SysMenu;
import com.student.manager.mapper.SysMenuMapper;
import com.student.manager.service.SysMenuService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Override
    public List<SysMenu> listAll(String keyword) {
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(SysMenu::getMenuName, keyword);
        }
        wrapper.orderByAsc(SysMenu::getSort);
        List<SysMenu> menus = list(wrapper);
        return buildMenuTree(menus);
    }

    @Override
    public List<SysMenu> buildMenuTree(List<SysMenu> menus) {
        Map<Long, List<SysMenu>> menuMap = menus.stream()
                .collect(Collectors.groupingBy(m -> m.getParentId() != null ? m.getParentId() : 0L));

        List<SysMenu> roots = menuMap.getOrDefault(0L, new ArrayList<>());
        for (SysMenu root : roots) {
            buildChildren(root, menuMap);
        }
        return roots;
    }

    private void buildChildren(SysMenu parent, Map<Long, List<SysMenu>> menuMap) {
        List<SysMenu> children = menuMap.getOrDefault(parent.getId(), new ArrayList<>());
        parent.setChildren(children);
        for (SysMenu child : children) {
            buildChildren(child, menuMap);
        }
    }

    @Override
    public void createMenu(SysMenu menu) {
        save(menu);
    }

    @Override
    public void updateMenu(Long id, SysMenu menu) {
        menu.setId(id);
        updateById(menu);
    }

    @Override
    public void deleteMenu(Long id) {
        removeById(id);
    }
}
