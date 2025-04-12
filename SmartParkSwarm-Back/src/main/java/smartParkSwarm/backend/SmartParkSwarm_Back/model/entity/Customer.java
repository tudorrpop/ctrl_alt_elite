package smartParkSwarm.backend.SmartParkSwarm_Back.model.entity;

import jakarta.persistence.*;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.enums.Role;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
public class Customer extends User implements Serializable {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dateOfRegistration;

    public Customer(String username, String password, LocalDate dateOfRegistration, Role role) {
        super(username, password, role);
        this.dateOfRegistration = dateOfRegistration;
    }

    public Customer(){}

    @Override
    public Long getId() {
        return id;
    }
}
