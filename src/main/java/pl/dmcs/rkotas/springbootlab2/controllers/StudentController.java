package pl.dmcs.rkotas.springbootlab2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.dmcs.rkotas.springbootlab2.dto.GradeDTO;
import pl.dmcs.rkotas.springbootlab2.dto.StudentDTO;
import pl.dmcs.rkotas.springbootlab2.dto.SubjectDTO;
import pl.dmcs.rkotas.springbootlab2.service.StudentService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    public List<StudentDTO> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable Long id) {
        StudentDTO dto = studentService.getStudentById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<StudentDTO> createStudent(@RequestBody StudentDTO dto) {
        StudentDTO created = studentService.createStudent(dto);
        return ResponseEntity.created(URI.create("/api/students/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> updateStudent(@PathVariable Long id, @RequestBody StudentDTO dto) {
        StudentDTO updated = studentService.updateStudent(id, dto);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        return studentService.deleteStudent(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
    @GetMapping("/me/subjects")
    public ResponseEntity<List<SubjectDTO>> getMySubjects() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<SubjectDTO> subjects = studentService.getSubjectsForStudentUsername(username);
        return ResponseEntity.ok(subjects);
    }

    @GetMapping("/me/grades")
    public ResponseEntity<List<GradeDTO>> getMyGrades() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<GradeDTO> grades = studentService.getGradesForStudentUsername(username);
        return ResponseEntity.ok(grades);
    }

}
