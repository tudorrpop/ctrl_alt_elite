package smartParkSwarm.backend.SmartParkSwarm_Back.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.enums.Role;

import java.io.Serializable;
import java.util.UUID;

@Entity
public class Customer extends User implements Serializable {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    @Getter
    private String firstName;
    @Setter
    @Getter
    private String lastName;
    @Setter
    @Getter
    private String email;
    @Setter
    @Getter
    private String phoneNumber;
    @Setter
    @Getter
    private String membership;
    @Setter
    @Getter
    private boolean active;
    @Setter
    @Getter
    private boolean parked;
    @Getter
    private String uuid;

    public Customer(String username, String password, Role role) {
        super(username, password, role);
        this.parked = false;
        this.active = true;
        this.uuid = UUID.randomUUID().toString();
    }

    public Customer(){}

    @Override
    public Long getId() {
        return id;
    }


}
