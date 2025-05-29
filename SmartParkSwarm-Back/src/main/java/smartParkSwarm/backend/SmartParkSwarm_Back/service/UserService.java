package smartParkSwarm.backend.SmartParkSwarm_Back.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.entity.Admin;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.entity.Customer;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.response.AdminModel;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.response.CustomerModel;
import smartParkSwarm.backend.SmartParkSwarm_Back.repository.AdminRepository;
import smartParkSwarm.backend.SmartParkSwarm_Back.repository.CustomerRepository;

import java.util.List;
import java.util.stream.Collectors;

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

    public CustomerModel fetchCustomer(Long id) {
        Customer customer = customerRepository.findCustomerById(id);
        return new CustomerModel(
                customer.getId(),
                customer.getUsername(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getPhoneNumber(),
                customer.getMembership(),
                customer.isActive(),
                customer.getUuid());
    }

    public AdminModel fetchAdmin(Long id) {
        Admin admin = adminRepository.findAdminById(id);
        return new AdminModel(
                admin.getId(),
                admin.getUsername());
    }

    public List<CustomerModel> fetchCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream().map(customer ->
                        new CustomerModel(
                                customer.getId(),
                                customer.getUsername(),
                                customer.getFirstName(),
                                customer.getLastName(),
                                customer.getEmail(),
                                customer.getPhoneNumber(),
                                customer.getMembership(),
                                customer.isActive(),
                                customer.getUuid()))
                .collect(Collectors.toList());
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
