package smartParkSwarm.backend.SmartParkSwarm_Back.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.Role;

import java.io.Serializable;

@Entity
@Getter
public class Customer extends User implements Serializable {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Customer(String username, String password, Role role) {
        super(username, password, role);
    }

    public Customer (){}

    @Override
    public Long getId() {
        return id;
    }
}
