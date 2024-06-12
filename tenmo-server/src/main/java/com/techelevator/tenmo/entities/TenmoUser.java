package com.techelevator.tenmo.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tenmo_user")
public class TenmoUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    int userId;

    @Column(name = "user_name")
    String userName;

    @Column(name = "password_hash")
    String passwordHash;

    @Column(name = "role")
    String role;

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
