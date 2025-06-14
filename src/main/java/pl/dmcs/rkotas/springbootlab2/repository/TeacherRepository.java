package pl.dmcs.rkotas.springbootlab2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.dmcs.rkotas.springbootlab2.model.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {}