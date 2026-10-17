package com.student.manager.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.student.manager.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dict")
public class SysDict extends BaseEntity {
    private String dictName;
    private String dictType;
    private String dictValue;
    private Integer sort;
    private Integer status;
    private String remark;
}
