package smartParkSwarm.backend.SmartParkSwarm_Back.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.enums.Role;

@MappedSuperclass
public abstract class User {


    @Column
    private String username;

    @Column
    private String password;

    @Column
    private Role role;

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public User(){}

    public abstract Long getId();

    public abstract String getUuid();
}
