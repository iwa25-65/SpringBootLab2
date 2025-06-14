package pl.dmcs.rkotas.springbootlab2.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.dmcs.rkotas.springbootlab2.model.Grade;
import pl.dmcs.rkotas.springbootlab2.repository.GradeRepository;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    private final GradeRepository gradeRepository;

    public StudentController(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    @GetMapping("/{id}/grades")
    @PreAuthorize("hasRole('STUDENT') && #id == principal.id") // Secure by student ID
    public List<Grade> getGrades(@PathVariable Long id) {
        return gradeRepository.findByStudentId(id);
    }
}