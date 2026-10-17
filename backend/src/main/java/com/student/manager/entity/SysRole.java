package com.student.manager.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.student.manager.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role")
public class SysRole extends BaseEntity {
    private String roleName;
    private String roleKey;
    private Integer sort;
    private Integer status;
    private String remark;

    @TableField(exist = false)
    private List<Long> menuIds;
}
