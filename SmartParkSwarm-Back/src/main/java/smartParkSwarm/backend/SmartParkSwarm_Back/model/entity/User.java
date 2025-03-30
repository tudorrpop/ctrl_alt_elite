package smartParkSwarm.backend.SmartParkSwarm_Back.model.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.Role;

@Getter
@MappedSuperclass
public abstract class User {

    private String username;
    private String password;
    private Role role;

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User(){}

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public abstract Long getId();
}
