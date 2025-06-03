package pl.dmcs.rkotas.springbootlab2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.dmcs.rkotas.springbootlab2.model.Student;
import pl.dmcs.rkotas.springbootlab2.model.Team;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {
    Student findById(long id);
    List<Student> findByTeamListContaining(Team team);
}
