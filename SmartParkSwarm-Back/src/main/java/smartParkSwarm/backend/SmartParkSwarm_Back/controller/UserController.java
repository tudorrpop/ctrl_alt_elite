package smartParkSwarm.backend.SmartParkSwarm_Back.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.response.AdminModel;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.response.CustomerModel;
import smartParkSwarm.backend.SmartParkSwarm_Back.service.UserService;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:8100", "http://192.168.1.128:8100", "http://smartparkswarm.salmonpebble-d8e2875c.polandcentral.azurecontainerapps.io:4200", "http://mobile.salmonpebble-d8e2875c.polandcentral.azurecontainerapps.io:8100"})
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

    @PutMapping("customer/user/{id}")
    public ResponseEntity<CustomerModel> updateCustomer(@PathVariable Long id, @RequestBody CustomerModel updatedCustomer){
        CustomerModel customer = userService.updateCustomer(id, updatedCustomer);
        return ResponseEntity.ok(customer);
    }
}
