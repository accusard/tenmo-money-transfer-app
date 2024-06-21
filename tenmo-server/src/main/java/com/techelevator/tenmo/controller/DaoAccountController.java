package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.exception.TransferExecption;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.TransferRequest;
import com.techelevator.tenmo.model.User;
import org.hibernate.TransactionException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
/**
 * Uses DAO pattern to access database
 * The DAO allows manual CRUD operations and
 * flexibility
 */
@PreAuthorize("isAuthenticated()")
@RestController
public class DaoAccountController {

    private final AccountDao accountDao;
    private final TransferDao transferDao;
    private final UserDao userDao;
    private Principal principal;

    public DaoAccountController(AccountDao accountDao, TransferDao transferDao, UserDao userDao) {
        this.accountDao = accountDao;
        this.transferDao = transferDao;
        this.userDao = userDao;
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(path = "/balance", method = RequestMethod.GET)
    public BigDecimal getBalance(Principal principal) {
        BigDecimal balance;

        try {
            int accountId = accountDao.getAccountId(getCurrentUserId(principal));
            balance = accountDao.getAccountBalance(accountId);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unable to get balance from server!.");
        }
        return balance;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/accounts")
    public List<Account> getAccount() {
        return accountDao.getAccounts();
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/send")
    public void transfer(@RequestBody TransferRequest transferRequest, Principal principal) {
        try {
            int accountFromId = accountDao.getAccountId(getCurrentUserId(principal));
            transferDao.transferTeBucks(accountFromId, transferRequest.getAccountToId(), transferRequest.getAmount());
        } catch (DaoException ignored) {

        } catch (TransferExecption e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unable to send transfer!, " + e.getMessage());
        }
    }

    public int getCurrentUserId(Principal principal) {
        String username = principal.getName();
        User user = userDao.getUserByUsername(username);
        return user.getId();
    }
}
