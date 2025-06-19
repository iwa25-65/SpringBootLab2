package pl.dmcs.rkotas.springbootlab2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.dmcs.rkotas.springbootlab2.dto.TeacherDTO;
import pl.dmcs.rkotas.springbootlab2.model.Teacher;
import pl.dmcs.rkotas.springbootlab2.repository.TeacherRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    public List<TeacherDTO> getAllTeachers() {
        return teacherRepository.findAll().stream()
            .map(t -> new TeacherDTO(t.getId(), t.getUser().getUsername()))
            .toList();
    }

    public TeacherDTO getTeacherById(Long id) {
        return teacherRepository.findById(id)
            .map(t -> new TeacherDTO(t.getId(), t.getUser().getUsername()))
            .orElse(null);
    }

    public TeacherDTO createTeacher(TeacherDTO dto) {
        return null;
    }

    public TeacherDTO updateTeacher(Long id, TeacherDTO dto) {
        return null;
    }

    public boolean deleteTeacher(Long id) {
        if (!teacherRepository.existsById(id)) return false;
        teacherRepository.deleteById(id);
        return true;
    }
}
