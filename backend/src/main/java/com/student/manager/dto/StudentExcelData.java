package com.student.manager.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class StudentExcelData {
    @ExcelProperty("学号")
    private String studentNo;

    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty("性别")
    private String gender;

    @ExcelProperty("年龄")
    private Integer age;

    @ExcelProperty("班级")
    private String className;

    @ExcelProperty("专业")
    private String major;

    @ExcelProperty("手机号")
    private String phone;

    @ExcelProperty("邮箱")
    private String email;
}
