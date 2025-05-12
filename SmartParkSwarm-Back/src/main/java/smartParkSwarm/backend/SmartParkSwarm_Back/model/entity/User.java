package smartParkSwarm.backend.SmartParkSwarm_Back.model.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.enums.Role;

@MappedSuperclass
public abstract class User {

    @Getter
    private String username;
    @Getter
    private String password;
    @Getter
    private Role role;

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User(){}

    public abstract Long getId();
}
