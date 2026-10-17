package com.student.manager.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.student.manager.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_menu")
public class SysMenu extends BaseEntity {
    private Long parentId;
    private String menuName;
    private Integer menuType;
    private String icon;
    private String path;
    private String perms;
    private Integer sort;
    private Integer visible;

    @TableField(exist = false)
    private List<SysMenu> children;
}
