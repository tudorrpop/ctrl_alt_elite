package smartParkSwarm.backend.SmartParkSwarm_Back.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.enums.Role;

@MappedSuperclass
public abstract class User {

    @Getter
    @Setter
    @Column
    private String username;
    @Getter
    @Setter
    @Column
    private String password;
    @Getter
    @Setter
    @Column
    private Role role;

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User(){}

    public abstract Long getId();

    public abstract String getUuid();
}
