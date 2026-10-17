package com.student.manager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.student.manager.entity.Student;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StudentMapper extends BaseMapper<Student> {
}
