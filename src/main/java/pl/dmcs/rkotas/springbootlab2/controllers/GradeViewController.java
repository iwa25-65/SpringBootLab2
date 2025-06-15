package pl.dmcs.rkotas.springbootlab2.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.dmcs.rkotas.springbootlab2.model.Grade;
import pl.dmcs.rkotas.springbootlab2.repository.GradeRepository;
import pl.dmcs.rkotas.springbootlab2.security.services.UserPrinciple;

import java.util.List;

@RestController
@RequestMapping("/api/grades")
public class GradeViewController {

    private final GradeRepository gradeRepository;

    public GradeViewController(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    // Student can view their own grades
    @GetMapping("/my")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<Grade>> getMyGrades(@AuthenticationPrincipal UserPrinciple principal) {
        List<Grade> grades = gradeRepository.findByStudentId(principal.getId());
        return ResponseEntity.ok(grades);
    }

    // Student can view grades for specific subject
    @GetMapping("/my/subjects/{subjectId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<Grade>> getMyGradesForSubject(
            @AuthenticationPrincipal UserPrinciple principal,
            @PathVariable Long subjectId) {
        List<Grade> grades = gradeRepository.findByStudentAndSubject(principal.getId(), subjectId);
        return ResponseEntity.ok(grades);
    }

    // Teacher can view all grades for their subjects
    @GetMapping("/taught")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<List<Grade>> getGradesForTaughtSubjects(
            @AuthenticationPrincipal UserPrinciple principal) {
        List<Grade> grades = gradeRepository.findByTeacherId(principal.getId());
        return ResponseEntity.ok(grades);
    }

    // Teacher can view grades for specific student in their subjects
    @GetMapping("/taught/students/{studentId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<List<Grade>> getGradesForStudentInTaughtSubjects(
            @AuthenticationPrincipal UserPrinciple principal,
            @PathVariable Long studentId) {
        // Additional validation can be added here to ensure the student is enrolled
        // in at least one of the teacher's subjects
        List<Grade> grades = gradeRepository.findByStudentIdAndTeacherId(studentId, principal.getId());
        return ResponseEntity.ok(grades);
    }
}