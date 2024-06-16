package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class AccountTransferDto {
    public int userId;
    public int userAccountId;
    public BigDecimal balance;
    public long transferId;
    public int transferTypeId;
    public int transferStatusId;
    public int accountFrom;
    public int accountTo;
    public BigDecimal amount;

    public AccountTransferDto(int userId, int userAccountId, BigDecimal balance, long transferId, int transferTypeId, int transferStatusId, int accountFrom, int accountTo, BigDecimal amount) {
        this.userId = userId;
        this.userAccountId = userAccountId;
        this.balance = balance;
        this.transferId = transferId;
        this.transferTypeId = transferTypeId;
        this.transferStatusId = transferStatusId;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
    }
}
