package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.entities.Account;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.exception.TransferExecption;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.math.BigDecimal;
import java.util.List;

@Component
public class jdbcTransferDao implements TransferDao {

    private final JdbcTemplate jdbcTemplate;
    private final PlatformTransactionManager transactionManager;

    public jdbcTransferDao(JdbcTemplate jdbcTemplate, PlatformTransactionManager transactionManager) {
        this.jdbcTemplate = jdbcTemplate;
        this.transactionManager = transactionManager;
    }

    @Override
    public void transferTeBucks(int accountFromId, int accountToId, BigDecimal amount) throws TransferExecption {
        String sql = "SELECT balance FROM account WHERE account_id = ?;";
        BigDecimal balance;
        try {
            balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, accountFromId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }

        checkTransferConditions(accountFromId, accountToId, amount, balance);

        // Starts a transaction so if a one of the update fails none of them get commited.
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);

        String updateFromAccount = "UPDATE account SET balance = balance - ? WHERE account_id = ?";
        String updateToAccount = "UPDATE account SET balance = balance + ? WHERE account_id = ?";
        String insertTransfer = "INSERT INTO transfer(transfer_type_id, transfer_status_id, account_from, account_to, amount)" +
                "VALUES ((SELECT transfer_type_id FROM transfer_type WHERE transfer_type_desc = 'Send')," +
                "(SELECT transfer_status_id FROM transfer_status WHERE transfer_status_desc = 'Approved')," +
                "?, ?, ?)";

        try {
            jdbcTemplate.update(updateFromAccount, amount, accountFromId);
            jdbcTemplate.update(updateToAccount, amount, accountToId);
            jdbcTemplate.update(insertTransfer, accountFromId, accountToId, amount);

            transactionManager.commit(status);

        } catch (CannotGetJdbcConnectionException e) {
            transactionManager.rollback(status);
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            transactionManager.rollback(status);
            throw new DaoException("Data integrity violation", e);
        }
    }

    private void checkTransferConditions(int accountFromId, int accountToId, BigDecimal amount, BigDecimal balance) throws TransferExecption {
        if (accountFromId == accountToId)
            throw new TransferExecption("Sending money to same account is not allowed!");

        if (balance.compareTo(amount) < 0)
            throw new TransferExecption("Insufficient funds!");

        if (amount.compareTo(BigDecimal.ZERO) < 0)
            throw new TransferExecption("amount must be greater than zero!");

    }
}
