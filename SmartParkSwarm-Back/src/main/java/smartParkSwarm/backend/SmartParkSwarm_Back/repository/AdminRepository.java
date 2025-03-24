package smartParkSwarm.backend.SmartParkSwarm_Back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.entity.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

}
