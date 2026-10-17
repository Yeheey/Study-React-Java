package com.student.manager.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.student.manager.entity.Student;

import java.util.List;

public interface StudentService extends IService<Student> {
    IPage<Student> pageList(String keyword, int page, int pageSize);
    void createStudent(Student student);
    void updateStudent(Long id, Student student);
    void deleteStudent(Long id);
    List<Student> listForExport(String keyword);
    void importStudents(List<Student> students);
}
