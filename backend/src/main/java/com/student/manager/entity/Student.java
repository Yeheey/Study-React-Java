package com.student.manager.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.student.manager.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("student")
public class Student extends BaseEntity {
    private String studentNo;
    private String name;
    private String gender;
    private Integer age;
    private String className;
    private String major;
    private String phone;
    private String email;
    private String address;
    private String avatar;
    private String status;
}
