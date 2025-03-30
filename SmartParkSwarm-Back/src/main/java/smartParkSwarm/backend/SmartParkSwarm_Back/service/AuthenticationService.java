package smartParkSwarm.backend.SmartParkSwarm_Back.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.Role;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.entity.Admin;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.entity.Customer;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.entity.User;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.request.AuthenticationRequest;
import smartParkSwarm.backend.SmartParkSwarm_Back.repository.AdminRepository;
import smartParkSwarm.backend.SmartParkSwarm_Back.repository.CustomerRepository;
import smartParkSwarm.backend.SmartParkSwarm_Back.utility.JwtUtil;

import java.util.Optional;

@Service
public class AuthenticationService {
    private final CustomerRepository customerRepository;
    private final AdminRepository adminRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(CustomerRepository customerRepository, AdminRepository adminRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.adminRepository = adminRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public String register(AuthenticationRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        if (request.getRole() == Role.CUSTOMER) {
            if (customerRepository.findCustomerByUsername(request.getUsername()).isPresent()) {
                throw new RuntimeException("Customer already exists");
            }
            customerRepository.save(new Customer(request.getUsername(), encodedPassword, request.getRole()));
        } else {
            if (adminRepository.findAdminByUsername(request.getUsername()).isPresent()) {
                throw new RuntimeException("Admin already exists");
            }
            adminRepository.save(new Admin(request.getUsername(), encodedPassword, request.getRole()));
        }

        return jwtUtil.generateToken(request.getUsername(), request.getRole());
    }

    public String login(AuthenticationRequest request) {
        Optional<User> user;

        if (request.getRole() == Role.CUSTOMER) {
            user = customerRepository.findCustomerByUsername(request.getUsername()).map(u -> u);
        } else {
            user = adminRepository.findAdminByUsername(request.getUsername()).map(u -> u);
        }

        if (user.isEmpty() || !passwordEncoder.matches(request.getPassword(), user.get().getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        return jwtUtil.generateToken(user.get().getUsername(), user.get().getRole());
    }
}
