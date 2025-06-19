package pl.dmcs.rkotas.springbootlab2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.dmcs.rkotas.springbootlab2.model.Teacher;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findByUserUsername(String username);
    Optional<Teacher> findByUser_Username(String username);

}
