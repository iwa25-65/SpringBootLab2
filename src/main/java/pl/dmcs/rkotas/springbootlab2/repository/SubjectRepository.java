package pl.dmcs.rkotas.springbootlab2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.dmcs.rkotas.springbootlab2.model.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    // Custom methods if needed
}