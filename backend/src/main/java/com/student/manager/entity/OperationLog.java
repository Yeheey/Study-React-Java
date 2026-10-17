package com.student.manager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_operation_log")
public class OperationLog {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String operator;
    private String module;
    private String type;
    private String method;
    private String url;
    private String ip;
    private Integer status;
    private Long costTime;
    private String errorMsg;
    private LocalDateTime createTime;
}
