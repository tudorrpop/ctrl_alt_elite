package smartParkSwarm.backend.SmartParkSwarm_Back.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.request.AuthenticationRequest;
import smartParkSwarm.backend.SmartParkSwarm_Back.service.AuthenticationService;

@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest) {
        return ResponseEntity.ok(authenticationService.login(authenticationRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthenticationRequest authenticationRequest) {
        return ResponseEntity.ok(authenticationService.register(authenticationRequest));
    }

}
