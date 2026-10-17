package com.student.manager.controller;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.student.manager.common.Log;
import com.student.manager.common.Result;
import com.student.manager.dto.StudentExcelData;
import com.student.manager.entity.Student;
import com.student.manager.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Tag(name = "学生管理")
@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @Operation(summary = "学生列表")
    @GetMapping
    @PreAuthorize("hasAuthority('student:list')")
    public Result<IPage<Student>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(studentService.pageList(keyword, page, pageSize));
    }

    @Operation(summary = "学生详情")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('student:query')")
    public Result<Student> getById(@PathVariable Long id) {
        return Result.ok(studentService.getById(id));
    }

    @Operation(summary = "新增学生")
    @PostMapping
    @Log(module = "学生管理", type = "新增")
    @PreAuthorize("hasAuthority('student:add')")
    public Result<Void> create(@RequestBody Student student) {
        studentService.createStudent(student);
        return Result.ok();
    }

    @Operation(summary = "修改学生")
    @PutMapping("/{id}")
    @Log(module = "学生管理", type = "修改")
    @PreAuthorize("hasAuthority('student:edit')")
    public Result<Void> update(@PathVariable Long id, @RequestBody Student student) {
        studentService.updateStudent(id, student);
        return Result.ok();
    }

    @Operation(summary = "删除学生")
    @DeleteMapping("/{id}")
    @Log(module = "学生管理", type = "删除")
    @PreAuthorize("hasAuthority('student:delete')")
    public Result<Void> delete(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return Result.ok();
    }

    @Operation(summary = "导出学生")
    @GetMapping("/export")
    @Log(module = "学生管理", type = "导出")
    @PreAuthorize("hasAuthority('student:export')")
    public void export(HttpServletResponse response, @RequestParam(required = false) String keyword) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("学生列表", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        List<Student> students = studentService.listForExport(keyword);
        List<StudentExcelData> excelData = new ArrayList<>();
        for (Student s : students) {
            StudentExcelData d = new StudentExcelData();
            d.setStudentNo(s.getStudentNo());
            d.setName(s.getName());
            d.setGender(s.getGender());
            d.setAge(s.getAge());
            d.setClassName(s.getClassName());
            d.setMajor(s.getMajor());
            d.setPhone(s.getPhone());
            d.setEmail(s.getEmail());
            excelData.add(d);
        }

        EasyExcel.write(response.getOutputStream(), StudentExcelData.class).sheet("学生列表").doWrite(excelData);
    }

    @Operation(summary = "导入学生")
    @PostMapping("/import")
    @Log(module = "学生管理", type = "导入")
    @PreAuthorize("hasAuthority('student:import')")
    public Result<Void> importStudents(@RequestParam("file") MultipartFile file) throws IOException {
        List<StudentExcelData> excelData = EasyExcel.read(file.getInputStream())
                .head(StudentExcelData.class).sheet().doReadSync();

        List<Student> students = new ArrayList<>();
        for (StudentExcelData d : excelData) {
            Student s = new Student();
            s.setStudentNo(d.getStudentNo());
            s.setName(d.getName());
            s.setGender(d.getGender());
            s.setAge(d.getAge());
            s.setClassName(d.getClassName());
            s.setMajor(d.getMajor());
            s.setPhone(d.getPhone());
            s.setEmail(d.getEmail());
            students.add(s);
        }

        studentService.importStudents(students);
        return Result.ok();
    }
}
