package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao {

    private final JdbcTemplate jdbcTemplate;
    private final PlatformTransactionManager transactionManager;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate, PlatformTransactionManager transactionManager) {
        this.jdbcTemplate = jdbcTemplate;
        this.transactionManager = transactionManager;
    }

    @Override
    public int getAccountId(int userId) {
        int accountId;
        String sql = "SELECT account_id FROM account WHERE user_id = ?";

        try {
            accountId = jdbcTemplate.<Integer>queryForObject(sql, int.class, userId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }

        return accountId;
    }

    @Override
    public BigDecimal getAccountBalance(int accountId) {
        BigDecimal balance = null;
        String sql = "SELECT balance FROM account WHERE account_id = ?;";

        try {
            balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, accountId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return balance;
    }

    public List<Account> getAccounts() {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT user_id, username, account_id FROM tenmo_user JOIN account USING (user_id);";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);
            while (rowSet.next()) {
                Account account = (mapRowToAccount(rowSet));
                accounts.add(account);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to Database", e);
        }
        return accounts;
    }

    private Account mapRowToAccount(SqlRowSet rowSet) {
        Account account = new Account();
        account.setUserId(rowSet.getInt("user_id"));
        account.setName(rowSet.getString("username"));
        account.setAccountId(rowSet.getInt("account_id"));
        return account;
    }

}
