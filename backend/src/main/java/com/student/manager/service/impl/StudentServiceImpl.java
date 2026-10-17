package com.student.manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.student.manager.entity.Student;
import com.student.manager.mapper.StudentMapper;
import com.student.manager.service.StudentService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    @Override
    public IPage<Student> pageList(String keyword, int page, int pageSize) {
        LambdaQueryWrapper<Student> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Student::getStudentNo, keyword)
                   .or().like(Student::getName, keyword);
        }
        wrapper.orderByDesc(Student::getCreateTime);
        return page(new Page<>(page, pageSize), wrapper);
    }

    @Override
    public void createStudent(Student student) {
        save(student);
    }

    @Override
    public void updateStudent(Long id, Student student) {
        student.setId(id);
        updateById(student);
    }

    @Override
    public void deleteStudent(Long id) {
        removeById(id);
    }

    @Override
    public List<Student> listForExport(String keyword) {
        LambdaQueryWrapper<Student> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Student::getStudentNo, keyword)
                   .or().like(Student::getName, keyword);
        }
        return list(wrapper);
    }

    @Override
    public void importStudents(List<Student> students) {
        saveBatch(students);
    }
}
