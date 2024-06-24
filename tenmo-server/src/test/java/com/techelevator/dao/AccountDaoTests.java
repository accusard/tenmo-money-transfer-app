package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.exception.TransferExecption;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.RegisterUserDto;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AccountDaoTests extends BaseDaoTests {
    protected static final Account account_1 = new Account(2001, 1001, "user1", BigDecimal.valueOf(1000));
    protected static final Account account_2 = new Account(2002, 1002, "user2", BigDecimal.valueOf(1000));



    private JdbcTransferDao tud;
    private JdbcAccountDao adm;
    private PlatformTransactionManager transactionManager;


    @Before
    public void setUp() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        transactionManager = mock(PlatformTransactionManager.class);
        TransactionStatus transactionStatus = mock(TransactionStatus.class);
        when(transactionManager.getTransaction(any(DefaultTransactionDefinition.class))).thenReturn(transactionStatus);

        tud = new JdbcTransferDao(jdbcTemplate, transactionManager);
        adm = new JdbcAccountDao(jdbcTemplate, transactionManager);
    }

    @Test
    public void getBalanceTest() throws TransferExecption {
        Assert.assertEquals(adm.getAccountBalance(account_1.getAccountId()), BigDecimal.valueOf(1000).setScale(2));
    }

    @Test(expected = TransferExecption.class)
    public void transferBucks_with_same_accountId() throws TransferExecption {
        tud.transferTeBucks(account_1.getAccountId(), account_1.getAccountId(), BigDecimal.valueOf(10));
    }

    @Test(expected = TransferExecption.class)
    public void transferBucks_with_negative_amount() throws TransferExecption {
        tud.transferTeBucks(account_1.getAccountId(), account_2.getAccountId(), BigDecimal.valueOf(-10));
    }

    @Test(expected = TransferExecption.class)
    public void transferBucks_with_insufficient_funds() throws TransferExecption {
        tud.transferTeBucks(account_1.getAccountId(), account_2.getAccountId(), BigDecimal.valueOf(1001));
    }

    @Test
    public void transferBucksTest() throws TransferExecption {
        tud.transferTeBucks(account_1.getAccountId(), account_2.getAccountId(), BigDecimal.valueOf(10));

        BigDecimal expectedBalance = BigDecimal.valueOf(1010).setScale(2);
        BigDecimal actualBalance = adm.getAccountBalance(2002).setScale(2);
        Assert.assertEquals(expectedBalance, actualBalance);

        expectedBalance = BigDecimal.valueOf(990).setScale(2);
        actualBalance = adm.getAccountBalance(2001).setScale(2);
        Assert.assertEquals(expectedBalance, actualBalance);

    }

    @Test
    public void getAccounts() {
        List<Account> accounts = adm.getAccounts();
        Assert.assertNotNull(accounts);
    }


}
