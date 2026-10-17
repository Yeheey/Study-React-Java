package com.student.manager.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.student.manager.common.Log;
import com.student.manager.common.Result;
import com.student.manager.dto.UserCreateRequest;
import com.student.manager.dto.UserUpdateRequest;
import com.student.manager.entity.SysUser;
import com.student.manager.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户管理")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final SysUserService userService;

    @Operation(summary = "用户列表")
    @GetMapping
    @PreAuthorize("hasAuthority('system:user:list')")
    public Result<IPage<SysUser>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(userService.pageList(keyword, page, pageSize));
    }

    @Operation(summary = "用户详情")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('system:user:query')")
    public Result<SysUser> getById(@PathVariable Long id) {
        SysUser user = userService.getById(id);
        if (user != null) user.setPassword(null);
        return Result.ok(user);
    }

    @Operation(summary = "新增用户")
    @PostMapping
    @Log(module = "用户管理", type = "新增")
    @PreAuthorize("hasAuthority('system:user:add')")
    public Result<Void> create(@Valid @RequestBody UserCreateRequest request) {
        userService.createUser(request);
        return Result.ok();
    }

    @Operation(summary = "修改用户")
    @PutMapping("/{id}")
    @Log(module = "用户管理", type = "修改")
    @PreAuthorize("hasAuthority('system:user:edit')")
    public Result<Void> update(@PathVariable Long id, @RequestBody UserUpdateRequest request) {
        userService.updateUser(id, request);
        return Result.ok();
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/{id}")
    @Log(module = "用户管理", type = "删除")
    @PreAuthorize("hasAuthority('system:user:delete')")
    public Result<Void> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return Result.ok();
    }

    @Operation(summary = "重置密码")
    @PostMapping("/{id}/reset-password")
    @Log(module = "用户管理", type = "重置密码")
    @PreAuthorize("hasAuthority('system:user:resetPwd')")
    public Result<Void> resetPassword(@PathVariable Long id) {
        userService.resetPassword(id);
        return Result.ok();
    }

    @Operation(summary = "分配角色")
    @PostMapping("/{id}/roles")
    @Log(module = "用户管理", type = "分配角色")
    @PreAuthorize("hasAuthority('system:user:assignRole')")
    public Result<Void> assignRoles(@PathVariable Long id, @RequestBody java.util.List<Long> roleIds) {
        userService.assignRoles(id, roleIds);
        return Result.ok();
    }
}
