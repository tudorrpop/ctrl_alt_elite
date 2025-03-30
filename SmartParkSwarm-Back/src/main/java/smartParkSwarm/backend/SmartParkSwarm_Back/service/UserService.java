package smartParkSwarm.backend.SmartParkSwarm_Back.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartParkSwarm.backend.SmartParkSwarm_Back.repository.AdminRepository;
import smartParkSwarm.backend.SmartParkSwarm_Back.repository.CustomerRepository;

@Service
public class UserService {

    @Autowired
    private final CustomerRepository customerRepository;

    @Autowired
    private final AdminRepository adminRepository;

    @Autowired
    public UserService(
            CustomerRepository customerRepository,
            AdminRepository adminRepository) {
        this.customerRepository = customerRepository;
        this.adminRepository = adminRepository;
    }

}
