package com.student.manager.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.student.manager.common.Log;
import com.student.manager.common.Result;
import com.student.manager.entity.SysRole;
import com.student.manager.service.SysRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "角色管理")
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final SysRoleService roleService;

    @Operation(summary = "角色列表")
    @GetMapping
    @PreAuthorize("hasAuthority('system:role:list')")
    public Result<IPage<SysRole>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(roleService.pageList(keyword, page, pageSize));
    }

    @Operation(summary = "新增角色")
    @PostMapping
    @Log(module = "角色管理", type = "新增")
    @PreAuthorize("hasAuthority('system:role:add')")
    public Result<Void> create(@RequestBody SysRole role) {
        roleService.createRole(role);
        return Result.ok();
    }

    @Operation(summary = "修改角色")
    @PutMapping("/{id}")
    @Log(module = "角色管理", type = "修改")
    @PreAuthorize("hasAuthority('system:role:edit')")
    public Result<Void> update(@PathVariable Long id, @RequestBody SysRole role) {
        roleService.updateRole(id, role);
        return Result.ok();
    }

    @Operation(summary = "删除角色")
    @DeleteMapping("/{id}")
    @Log(module = "角色管理", type = "删除")
    @PreAuthorize("hasAuthority('system:role:delete')")
    public Result<Void> delete(@PathVariable Long id) {
        roleService.deleteRole(id);
        return Result.ok();
    }

    @Operation(summary = "分配菜单权限")
    @PostMapping("/{id}/menus")
    @Log(module = "角色管理", type = "分配菜单")
    @PreAuthorize("hasAuthority('system:role:assignMenu')")
    public Result<Void> assignMenus(@PathVariable Long id, @RequestBody AssignMenuRequest request) {
        roleService.assignMenus(id, request.getMenuIds());
        return Result.ok();
    }

    @lombok.Data
    public static class AssignMenuRequest {
        private List<Long> menuIds;
    }
}
