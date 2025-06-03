package pl.dmcs.rkotas.springbootlab2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.dmcs.rkotas.springbootlab2.model.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    Address findById(long id);
}
