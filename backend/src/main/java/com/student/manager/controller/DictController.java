package com.student.manager.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.student.manager.common.Log;
import com.student.manager.common.Result;
import com.student.manager.entity.SysDict;
import com.student.manager.service.SysDictService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "字典管理")
@RestController
@RequestMapping("/api/dicts")
@RequiredArgsConstructor
public class DictController {

    private final SysDictService dictService;

    @Operation(summary = "字典列表")
    @GetMapping
    @PreAuthorize("hasAuthority('system:dict:list')")
    public Result<IPage<SysDict>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(dictService.pageList(keyword, page, pageSize));
    }

    @Operation(summary = "新增字典")
    @PostMapping
    @Log(module = "字典管理", type = "新增")
    @PreAuthorize("hasAuthority('system:dict:add')")
    public Result<Void> create(@RequestBody SysDict dict) {
        dictService.createDict(dict);
        return Result.ok();
    }

    @Operation(summary = "修改字典")
    @PutMapping("/{id}")
    @Log(module = "字典管理", type = "修改")
    @PreAuthorize("hasAuthority('system:dict:edit')")
    public Result<Void> update(@PathVariable Long id, @RequestBody SysDict dict) {
        dictService.updateDict(id, dict);
        return Result.ok();
    }

    @Operation(summary = "删除字典")
    @DeleteMapping("/{id}")
    @Log(module = "字典管理", type = "删除")
    @PreAuthorize("hasAuthority('system:dict:delete')")
    public Result<Void> delete(@PathVariable Long id) {
        dictService.deleteDict(id);
        return Result.ok();
    }
}
