package smartParkSwarm.backend.SmartParkSwarm_Back.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.enums.Role;

import java.io.Serializable;
import java.util.UUID;

@Entity
public class Customer extends User implements Serializable {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean parked;
    private String uuid;

    public Customer(String username, String password, Role role) {
        super(username, password, role);
        this.parked = false;
        this.uuid = UUID.randomUUID().toString();
    }

    public Customer(){}

    @Override
    public Long getId() {
        return id;
    }

    public boolean isParked() {
        return parked;
    }

    public void setParked(boolean parked) {
        this.parked = parked;
    }
}
