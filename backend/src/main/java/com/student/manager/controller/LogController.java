package com.student.manager.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.student.manager.common.Result;
import com.student.manager.entity.OperationLog;
import com.student.manager.service.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Tag(name = "操作日志")
@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class LogController {

    private final LogService logService;

    @Operation(summary = "日志列表")
    @GetMapping
    @PreAuthorize("hasAuthority('system:log:list')")
    public Result<IPage<OperationLog>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        LambdaQueryWrapper<OperationLog> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(OperationLog::getOperator, keyword)
                   .or().like(OperationLog::getModule, keyword);
        }
        wrapper.orderByDesc(OperationLog::getCreateTime);
        return Result.ok(logService.page(new Page<>(page, pageSize), wrapper));
    }
}
