package pl.dmcs.rkotas.springbootlab2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.dmcs.rkotas.springbootlab2.dto.GradeDTO;
import pl.dmcs.rkotas.springbootlab2.model.Grade;
import pl.dmcs.rkotas.springbootlab2.model.Student;
import pl.dmcs.rkotas.springbootlab2.model.Subject;
import pl.dmcs.rkotas.springbootlab2.repository.GradeRepository;
import pl.dmcs.rkotas.springbootlab2.repository.StudentRepository;
import pl.dmcs.rkotas.springbootlab2.repository.SubjectRepository;

import java.util.List;
import java.util.Optional;

@Service
public class GradeService {

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SubjectRepository subjectRepository;


    public List<GradeDTO> getAllGrades() {
        return gradeRepository.findAll().stream()
            .map(g -> new GradeDTO(g.getId(), g.getSubject().getName(), g.getValue()))
            .toList();
    }

    public GradeDTO getGradeById(Long id) {
        return gradeRepository.findById(id)
            .map(g -> new GradeDTO(g.getId(), g.getSubject().getName(), g.getValue()))
            .orElse(null);
    }

    public GradeDTO createGrade(GradeDTO dto) {
        return null;
    }

    public GradeDTO updateGrade(Long id, GradeDTO dto) {
        return null;
    }

    public boolean deleteGrade(Long id) {
        if (!gradeRepository.existsById(id)) return false;
        gradeRepository.deleteById(id);
        return true;
    }

    public GradeDTO createGradeFromDTO(GradeDTO dto) {
        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Subject subject = subjectRepository.findById(dto.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        Grade grade = new Grade(dto.getValue(), dto.getComment(), student, subject);
        Grade saved = gradeRepository.save(grade);

        return new GradeDTO(
                saved.getId(),
                saved.getValue(),
                saved.getComment(),
                student.getId(),
                subject.getId(),
                subject.getName()
        );
    }

}
