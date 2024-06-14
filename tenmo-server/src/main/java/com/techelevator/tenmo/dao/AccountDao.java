package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {

    int getAccountId(int userId);

    BigDecimal getAccountBalance(int accountId);

    List<Account> getAccounts();
}
