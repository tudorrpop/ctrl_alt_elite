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
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String membership;
    private boolean active;
    private boolean parked;
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

    public boolean isParked() {
        return parked;
    }

    public void setParked(boolean parked) {
        this.parked = parked;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getMembership() {
        return membership;
    }

    public boolean isActive() {
        return active;
    }

    public String getUuid() {
        return uuid;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setMembership(String membership) {
        this.membership = membership;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
