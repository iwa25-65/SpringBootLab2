package pl.dmcs.rkotas.springbootlab2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.dmcs.rkotas.springbootlab2.dto.GradeDTO;
import pl.dmcs.rkotas.springbootlab2.dto.StudentDTO;
import pl.dmcs.rkotas.springbootlab2.dto.SubjectDTO;
import pl.dmcs.rkotas.springbootlab2.model.Student;
import pl.dmcs.rkotas.springbootlab2.repository.StudentRepository;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public List<StudentDTO> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(s -> new StudentDTO(s.getId(), s.getUser().getUsername()))
                .toList();
    }

    public StudentDTO getStudentById(Long id) {
        return studentRepository.findById(id)
                .map(s -> new StudentDTO(s.getId(), s.getUser().getUsername()))
                .orElse(null);
    }

    public StudentDTO createStudent(StudentDTO dto) {
        // Tu peux compléter ici si tu veux créer un étudiant à partir du DTO
        return null;
    }

    public StudentDTO updateStudent(Long id, StudentDTO dto) {
        // Tu peux compléter ici si tu veux modifier un étudiant
        return null;
    }

    public boolean deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) return false;
        studentRepository.deleteById(id);
        return true;
    }

    // ✅ Notes d'un étudiant par son username (utilisé pour l'affichage)
    public List<GradeDTO> getGradesForStudentUsername(String username) {
        Student student = studentRepository.findByUser_Username(username)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        return student.getEnrolledSubjects().stream()
                .flatMap(subject -> subject.getGrades().stream()
                        .filter(grade -> grade.getStudent().equals(student)))
                .map(grade -> new GradeDTO(
                        grade.getId(),
                        grade.getValue(),
                        grade.getComment(),
                        grade.getStudent().getId(),
                        grade.getSubject().getId(),
                        grade.getSubject().getName()
                ))
                .toList();
    }

    // ✅ Matières auxquelles l'étudiant est inscrit
    public List<SubjectDTO> getSubjectsForStudentUsername(String username) {
        Student student = studentRepository.findByUser_Username(username)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        return student.getEnrolledSubjects().stream()
                .map(s -> new SubjectDTO(
                        s.getId(),
                        s.getName(),
                        s.getTeacher().getUser().getUsername(),
                        s.getCode()
                ))
                .toList();
    }
}
