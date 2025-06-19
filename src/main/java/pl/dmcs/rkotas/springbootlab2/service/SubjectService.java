package pl.dmcs.rkotas.springbootlab2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.dmcs.rkotas.springbootlab2.dto.SubjectDTO;
import pl.dmcs.rkotas.springbootlab2.model.Subject;
import pl.dmcs.rkotas.springbootlab2.model.Teacher;
import pl.dmcs.rkotas.springbootlab2.repository.SubjectRepository;
import pl.dmcs.rkotas.springbootlab2.repository.TeacherRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private TeacherRepository teacherRepository;


    public List<SubjectDTO> getAllSubjects() {
        return subjectRepository.findAll().stream().map(this::toDTO).toList();
    }

    public SubjectDTO getSubjectById(Long id) {
        return subjectRepository.findById(id).map(this::toDTO).orElse(null);
    }

    public SubjectDTO createSubjectForTeacher(SubjectDTO dto, String username) {
        Teacher teacher = teacherRepository.findByUser_Username(username)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        Subject subject = new Subject();
        subject.setName(dto.getName());
        subject.setCode(dto.getCode()); // ✅ important !
        subject.setTeacher(teacher);

        Subject saved = subjectRepository.save(subject);

        return new SubjectDTO(saved.getId(), saved.getName(), saved.getTeacher().getUser().getUsername(), saved.getCode());
    }



    public SubjectDTO updateSubject(Long id, SubjectDTO dto) {
        Optional<Subject> opt = subjectRepository.findById(id);
        if (opt.isEmpty()) return null;
        Subject subject = opt.get();
        subject.setName(dto.getName());
        Optional<Teacher> teacher = teacherRepository.findAll()
            .stream().filter(t -> t.getUser().getUsername().equals(dto.getTeacherUsername())).findFirst();
        teacher.ifPresent(subject::setTeacher);
        return toDTO(subjectRepository.save(subject));
    }

    public boolean deleteSubject(Long id) {
        if (!subjectRepository.existsById(id)) return false;
        subjectRepository.deleteById(id);
        return true;
    }

    private SubjectDTO toDTO(Subject s) {
        return new SubjectDTO(
                s.getId(),
                s.getName(),
                s.getTeacher().getUser().getUsername(),
                s.getCode() // ✅ ajoute le champ manquant
        );
    }

    public List<SubjectDTO> getSubjectsByTeacherUsername(String username) {
        return subjectRepository.findAll().stream()
                .filter(subject -> subject.getTeacher().getUser().getUsername().equals(username))
                .map(s -> new SubjectDTO(
                        s.getId(),
                        s.getName(),
                        s.getTeacher().getUser().getUsername(),
                        s.getCode() // ✅ 4e argument requis
                ))

                .toList();
    }

}
