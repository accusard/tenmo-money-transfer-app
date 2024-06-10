package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferRequest;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.services.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
@PreAuthorize("isAuthenticated()")
@RestController
public class AccountController {

    private final AccountDao accountDao;
    private final UserDao userDao;
    private Principal principal;

    public AccountController(AccountDao accountDao, UserDao userDao) {
        this.accountDao = accountDao;
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
            accountDao.transferTeBucks(accountFromId, transferRequest.getAccountToId(), transferRequest.getAmount());
        } catch (DaoException ignored) {}
    }

    public int getCurrentUserId(Principal principal) {
        String username = principal.getName();
        User user = userDao.getUserByUsername(username);
        return user.getId();
    }
}
