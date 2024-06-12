package com.techelevator.tenmo.entities;

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

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private TenmoUser tenmoUser;

    @OneToMany(mappedBy = "accountFrom")
    private List<Transfer> transfersFrom;

    @OneToMany(mappedBy = "accountTo")
    private List<Transfer> transfersTo;

    public TenmoUser getTenmoUser() {
        return tenmoUser;
    }

    public void setTenmoUser(TenmoUser tenmoUser) {
        this.tenmoUser = tenmoUser;
    }

    public List<Transfer> getTransfersFrom() {
        return transfersFrom;
    }

    public void setTransfersFrom(List<Transfer> transfersFrom) {
        this.transfersFrom = transfersFrom;
    }

    public List<Transfer> getTransfersTo() {
        return transfersTo;
    }

    public void setTransfersTo(List<Transfer> transfersTo) {
        this.transfersTo = transfersTo;
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
