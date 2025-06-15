package pl.dmcs.rkotas.springbootlab2.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.dmcs.rkotas.springbootlab2.model.Grade;
import pl.dmcs.rkotas.springbootlab2.repository.GradeRepository;
import pl.dmcs.rkotas.springbootlab2.repository.SubjectRepository;
import pl.dmcs.rkotas.springbootlab2.security.services.UserPrinciple;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    private final GradeRepository gradeRepository;
    private final SubjectRepository subjectRepository;

    public TeacherController(GradeRepository gradeRepository,
                             SubjectRepository subjectRepository) {
        this.gradeRepository = gradeRepository;
        this.subjectRepository = subjectRepository;
    }

    @PostMapping("/grades")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> assignGrade(
            @AuthenticationPrincipal UserPrinciple principal,
            @RequestBody Grade grade) {

        // Verify the teacher teaches this subject
        if (!grade.getSubject().getTeacher().getId().equals(principal.getId())) {
            return ResponseEntity.status(403)
                    .body("You can only grade your own subjects");
        }

        Grade savedGrade = gradeRepository.save(grade);
        return ResponseEntity.ok(savedGrade);
    }
}