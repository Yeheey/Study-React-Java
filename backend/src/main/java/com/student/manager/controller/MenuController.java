package com.student.manager.controller;

import com.student.manager.common.Log;
import com.student.manager.common.Result;
import com.student.manager.entity.SysMenu;
import com.student.manager.service.SysMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "菜单管理")
@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {

    private final SysMenuService menuService;

    @Operation(summary = "菜单列表")
    @GetMapping
    @PreAuthorize("hasAuthority('system:menu:list')")
    public Result<List<SysMenu>> list(@RequestParam(required = false) String keyword) {
        return Result.ok(menuService.listAll(keyword));
    }

    @Operation(summary = "新增菜单")
    @PostMapping
    @Log(module = "菜单管理", type = "新增")
    @PreAuthorize("hasAuthority('system:menu:add')")
    public Result<Void> create(@RequestBody SysMenu menu) {
        menuService.createMenu(menu);
        return Result.ok();
    }

    @Operation(summary = "修改菜单")
    @PutMapping("/{id}")
    @Log(module = "菜单管理", type = "修改")
    @PreAuthorize("hasAuthority('system:menu:edit')")
    public Result<Void> update(@PathVariable Long id, @RequestBody SysMenu menu) {
        menuService.updateMenu(id, menu);
        return Result.ok();
    }

    @Operation(summary = "删除菜单")
    @DeleteMapping("/{id}")
    @Log(module = "菜单管理", type = "删除")
    @PreAuthorize("hasAuthority('system:menu:delete')")
    public Result<Void> delete(@PathVariable Long id) {
        menuService.deleteMenu(id);
        return Result.ok();
    }
}
