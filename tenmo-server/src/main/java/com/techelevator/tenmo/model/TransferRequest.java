package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class TransferRequest {
    private int accountToId;
    private BigDecimal amount;

    public TransferRequest(BigDecimal amount, int accountToId) {
        this.amount = amount;
        this.accountToId = accountToId;
    }

    public int getAccountToId() {
        return accountToId;
    }

    public void setAccountToId(int accountToId) {
        this.accountToId = accountToId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
