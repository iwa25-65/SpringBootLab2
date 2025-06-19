package pl.dmcs.rkotas.springbootlab2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.dmcs.rkotas.springbootlab2.model.Subject;

import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    Optional<Subject> findByCode(String code);

}