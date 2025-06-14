package pl.dmcs.rkotas.springbootlab2.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.dmcs.rkotas.springbootlab2.model.Subject;
import pl.dmcs.rkotas.springbootlab2.model.Grade;
import pl.dmcs.rkotas.springbootlab2.repository.SubjectRepository;
import pl.dmcs.rkotas.springbootlab2.repository.GradeRepository;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {
    private final SubjectRepository subjectRepository;
    private final GradeRepository gradeRepository;

    public TeacherController(SubjectRepository subjectRepository, GradeRepository gradeRepository) {
        this.subjectRepository = subjectRepository;
        this.gradeRepository = gradeRepository;
    }

    @PostMapping("/subjects")
    @PreAuthorize("hasRole('TEACHER')")
    public Subject addSubject(@RequestBody Subject subject) {
        return subjectRepository.save(subject);
    }

    @PostMapping("/grades")
    @PreAuthorize("hasRole('TEACHER')")
    public Grade assignGrade(@RequestBody Grade grade) {
        return gradeRepository.save(grade);
    }
}
