package smartParkSwarm.backend.SmartParkSwarm_Back.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import smartParkSwarm.backend.SmartParkSwarm_Back.service.UserService;

@RestController
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public ResponseEntity<?> login(){
        return (ResponseEntity<?>) ResponseEntity.ok();
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(){
        return (ResponseEntity<?>) ResponseEntity.ok();
    }

}
