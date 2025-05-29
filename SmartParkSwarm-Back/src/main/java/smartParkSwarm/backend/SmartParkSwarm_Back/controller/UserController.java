package smartParkSwarm.backend.SmartParkSwarm_Back.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.response.AdminModel;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.response.CustomerModel;
import smartParkSwarm.backend.SmartParkSwarm_Back.service.UserService;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("customer/users")
    public ResponseEntity<List<CustomerModel>> fetchCustomers(){
        List<CustomerModel> users = userService.fetchCustomers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("admin/user/{id}")
    public ResponseEntity<AdminModel> fetchAdmin(@PathVariable Long id){
        AdminModel admin = userService.fetchAdmin(id);
        return ResponseEntity.ok(admin);
    }

    @GetMapping("customer/user/{id}")
    public ResponseEntity<CustomerModel> fetchCustomer(@PathVariable Long id){
        CustomerModel user = userService.fetchCustomer(id);
        return ResponseEntity.ok(user);
    }
}
