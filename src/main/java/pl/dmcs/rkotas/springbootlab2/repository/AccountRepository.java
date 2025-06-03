package pl.dmcs.rkotas.springbootlab2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.dmcs.rkotas.springbootlab2.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

}
