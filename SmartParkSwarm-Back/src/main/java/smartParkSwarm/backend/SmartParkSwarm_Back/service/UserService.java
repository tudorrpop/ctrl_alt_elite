package smartParkSwarm.backend.SmartParkSwarm_Back.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.entity.Customer;
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

    public boolean markParked(String uuid) {
        Customer customer = customerRepository.findCustomerByUuid(uuid);

        boolean isParked = false;

        if (customer != null) {
            isParked = customer.isParked();

            if (!isParked) {
                customer.setParked(true);
                customerRepository.save(customer);
                return true;
            }

            return false;
        }
        return isParked;
    }

    public boolean unmarkParked(String uuid) {
        Customer customer = customerRepository.findCustomerByUuid(uuid);

        boolean isParked = false;

        if (customer != null) {
            isParked = customer.isParked();

            if (isParked) {
                customer.setParked(false);
                customerRepository.save(customer);
                return true;
            }

            return false;
        }
        return isParked;
    }


}
