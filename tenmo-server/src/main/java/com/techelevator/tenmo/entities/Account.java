package com.techelevator.tenmo.entities;


import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

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

    @ManyToOne()
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private TenmoUser tenmoUser;

    @OneToMany(mappedBy = "accountTo")
    private Set<Transfer> transfersTo;

    @OneToMany(mappedBy = "accountFrom")
    private Set<Transfer> transfersFrom;

    public TenmoUser getTenmoUser() {
        return tenmoUser;
    }

    public void setTenmoUser(TenmoUser tenmoUser) {
        this.tenmoUser = tenmoUser;
    }

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
