package pl.dmcs.rkotas.springbootlab2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.dmcs.rkotas.springbootlab2.dto.GradeDTO;
import pl.dmcs.rkotas.springbootlab2.dto.SubjectDTO;
import pl.dmcs.rkotas.springbootlab2.dto.TeacherDTO;
import pl.dmcs.rkotas.springbootlab2.service.GradeService;
import pl.dmcs.rkotas.springbootlab2.service.SubjectService;
import pl.dmcs.rkotas.springbootlab2.service.TeacherService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;
    @Autowired
    private SubjectService subjectService;

    @Autowired
    private GradeService gradeService;

    @GetMapping
    public List<TeacherDTO> getAllTeachers() {
        return teacherService.getAllTeachers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherDTO> getTeacherById(@PathVariable Long id) {
        TeacherDTO dto = teacherService.getTeacherById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<TeacherDTO> createTeacher(@RequestBody TeacherDTO dto) {
        TeacherDTO created = teacherService.createTeacher(dto);
        return ResponseEntity.created(URI.create("/api/teachers/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeacherDTO> updateTeacher(@PathVariable Long id, @RequestBody TeacherDTO dto) {
        TeacherDTO updated = teacherService.updateTeacher(id, dto);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long id) {
        return teacherService.deleteTeacher(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/subjects")
    public ResponseEntity<List<SubjectDTO>> getMySubjects() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<SubjectDTO> subjects = subjectService.getSubjectsByTeacherUsername(username);
        return ResponseEntity.ok(subjects);
    }

    @PostMapping("/grades")
    public ResponseEntity<GradeDTO> addGrade(@RequestBody GradeDTO gradeDTO) {
        GradeDTO saved = gradeService.createGradeFromDTO(gradeDTO);
        return ResponseEntity.ok(saved);
    }
}
