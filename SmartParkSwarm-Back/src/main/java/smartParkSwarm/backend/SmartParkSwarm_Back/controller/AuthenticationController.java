package smartParkSwarm.backend.SmartParkSwarm_Back.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.request.AuthenticationRequest;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.response.UserOverviewModel;
import smartParkSwarm.backend.SmartParkSwarm_Back.service.AuthenticationService;

@CrossOrigin(origins = "http://192.168.1.129:8100")
@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<UserOverviewModel> login(@RequestBody AuthenticationRequest authenticationRequest) {
        return ResponseEntity.ok(authenticationService.login(authenticationRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<UserOverviewModel> register(@RequestBody AuthenticationRequest authenticationRequest) {
        return ResponseEntity.ok(authenticationService.register(authenticationRequest));
    }

}
