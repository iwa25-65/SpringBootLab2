package pl.dmcs.rkotas.springbootlab2.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.dmcs.rkotas.springbootlab2.dto.SubjectDTO;
import pl.dmcs.rkotas.springbootlab2.model.Student;
import pl.dmcs.rkotas.springbootlab2.model.Subject;
import pl.dmcs.rkotas.springbootlab2.repository.StudentRepository;
import pl.dmcs.rkotas.springbootlab2.repository.SubjectRepository;

import java.util.List;
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

    // ✅ Inscription à une matière par l'étudiant connecté
    @PostMapping("/subjects/{subjectId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Void> enrollSelf(@PathVariable Long subjectId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Student student = studentRepository.findByUser_Username(username)
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

    // ✅ Désinscription par l'étudiant connecté
    @DeleteMapping("/subjects/{subjectId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Void> unenrollSelf(@PathVariable Long subjectId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Student student = studentRepository.findByUser_Username(username)
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

    // ✅ Liste des matières de l'étudiant connecté
    @GetMapping("/subjects")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<SubjectDTO>> getMySubjects() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Student student = studentRepository.findByUser_Username(username)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        List<SubjectDTO> subjects = student.getEnrolledSubjects().stream()
                .map(s -> new SubjectDTO(
                        s.getId(),
                        s.getName(),
                        s.getTeacher().getUser().getUsername(),
                        s.getCode()
                ))
                .toList();

        return ResponseEntity.ok(subjects);
    }
    @PostMapping("/subjects/code/{code}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Void> enrollBySubjectCode(@PathVariable String code) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Student student = studentRepository.findByUser_Username(username)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Subject subject = subjectRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        if (student.getEnrolledSubjects().contains(subject)) {
            return ResponseEntity.badRequest().build();
        }

        student.enrollInSubject(subject);
        studentRepository.save(student);
        return ResponseEntity.ok().build();
    }


}
