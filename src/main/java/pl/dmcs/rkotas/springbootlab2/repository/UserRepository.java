package pl.dmcs.rkotas.springbootlab2.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.dmcs.rkotas.springbootlab2.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email); // Ajout requis
}
