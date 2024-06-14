package com.techelevator.tenmo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    int accountId;

    @Column(name = "user_id")
    int userId;
    @Column(name = "balance")
    BigDecimal balance;

//    @ManyToOne()
//    @JsonIgnore
//    private TenmoUser tenmoUser;

//    public TenmoUser getTenmoUser() {
//        return tenmoUser;
//    }
//
//    public void setTenmoUser(TenmoUser tenmoUser) {
//        this.tenmoUser = tenmoUser;
//    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
