package smartParkSwarm.backend.SmartParkSwarm_Back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.entity.Customer;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findCustomerByUsername(String username);

    Customer findCustomerByUuid(String uuid);

    //Customer findCustomerById(Long id);
}
