package com.techelevator.tenmo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;

public class Account {
    private int accountId;
    private int userId;
    private String name;
    @JsonIgnore
    private BigDecimal balance;

    public Account(int accountId, int userId, String name, BigDecimal balance) {
        this.accountId = accountId;
        this.userId = userId;
        this.name = name;
        this.balance = balance;
    }

    public Account() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
