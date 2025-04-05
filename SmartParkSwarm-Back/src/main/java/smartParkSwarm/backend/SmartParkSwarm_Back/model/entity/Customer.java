package smartParkSwarm.backend.SmartParkSwarm_Back.model.entity;

import jakarta.persistence.*;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.enums.Role;

import java.io.Serializable;

@Entity
public class Customer extends User implements Serializable {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Customer(String username, String password, Role role) {
        super(username, password, role);
    }

    public Customer(){}

    @Override
    public Long getId() {
        return id;
    }
}
