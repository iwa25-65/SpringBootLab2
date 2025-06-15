package pl.dmcs.rkotas.springbootlab2.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.dmcs.rkotas.springbootlab2.model.Student;
import pl.dmcs.rkotas.springbootlab2.model.Subject;
import pl.dmcs.rkotas.springbootlab2.repository.StudentRepository;
import pl.dmcs.rkotas.springbootlab2.repository.SubjectRepository;

import java.util.Set;

@RestController
@RequestMapping("/api/enrollments")
@Transactional
public class EnrollmentController {

    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;

    public EnrollmentController(StudentRepository studentRepository,
                                SubjectRepository subjectRepository) {
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
    }

    @PostMapping("/{studentId}/subjects/{subjectId}")
    @PreAuthorize("hasRole('STUDENT') && #studentId == principal.id")
    public ResponseEntity<Void> enrollStudent(
            @PathVariable Long studentId,
            @PathVariable Long subjectId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        if (student.getEnrolledSubjects().contains(subject)) {
            return ResponseEntity.badRequest().build();
        }

        student.enrollInSubject(subject);
        studentRepository.save(student);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{studentId}/subjects/{subjectId}")
    @PreAuthorize("hasRole('STUDENT') && #studentId == principal.id")
    public ResponseEntity<Void> unenrollStudent(
            @PathVariable Long studentId,
            @PathVariable Long subjectId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        if (!student.getEnrolledSubjects().contains(subject)) {
            return ResponseEntity.notFound().build();
        }

        student.unenrollFromSubject(subject);
        studentRepository.save(student);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{studentId}/subjects")
    @PreAuthorize("hasRole('STUDENT') && #studentId == principal.id")
    public ResponseEntity<Set<Subject>> getEnrolledSubjects(
            @PathVariable Long studentId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return ResponseEntity.ok(student.getEnrolledSubjects());
    }
}