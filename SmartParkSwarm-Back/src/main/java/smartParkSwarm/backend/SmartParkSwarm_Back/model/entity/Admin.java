package smartParkSwarm.backend.SmartParkSwarm_Back.model.entity;

import jakarta.persistence.*;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.enums.Role;

import java.io.Serializable;

@Entity
public class Admin extends User implements Serializable {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Admin(String username, String password, Role role) {
        super(username, password, role);
    }

    public Admin(){}

    @Override
    public Long getId() {
        return id;
    }

    public String getUuid() {return null;}
}
