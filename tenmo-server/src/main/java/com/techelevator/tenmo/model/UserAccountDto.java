package com.techelevator.tenmo.model;

import javax.persistence.Column;
import java.math.BigDecimal;

public class UserAccountDto {
    public int userId;
    public String userName;
    public int accountId;
    public BigDecimal balance;

    public UserAccountDto(int userId, String userName, int accountId, BigDecimal balance) {
        this.userId = userId;
        this.userName = userName;
        this.accountId = accountId;
        this.balance = balance;
    }
}
