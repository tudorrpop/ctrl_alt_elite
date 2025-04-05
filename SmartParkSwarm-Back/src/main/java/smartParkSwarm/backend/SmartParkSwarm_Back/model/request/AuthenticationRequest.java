package smartParkSwarm.backend.SmartParkSwarm_Back.model.request;

import smartParkSwarm.backend.SmartParkSwarm_Back.model.enums.Role;

public class AuthenticationRequest {
    private String username;
    private String password;
    private Role role;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }
}