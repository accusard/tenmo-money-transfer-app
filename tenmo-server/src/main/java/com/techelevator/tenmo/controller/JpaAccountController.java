package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.entities.Account;
import com.techelevator.tenmo.entities.TenmoUser;
import com.techelevator.tenmo.entities.Transfer;
import com.techelevator.tenmo.model.TransferRequest;
import com.techelevator.tenmo.model.AccountTransferDto;
import com.techelevator.tenmo.model.UserAccountDto;
import com.techelevator.tenmo.services.AccountTransferService;
import com.techelevator.tenmo.services.UserAccountService;
import com.techelevator.tenmo.services.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * Uses Spring Data JPA pattern to access database.
 * Provides boilerplate code through repositories
 * to perform common CRUD operations
 */
@RestController
@RequestMapping("account/")
public class JpaAccountController {

    @Autowired
    TransferService transferService;
    @Autowired
    UserAccountService userAccountService;
    @Autowired
    AccountTransferService accountTransferService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping("transfers/{id}")
    public Transfer getTransfer(@PathVariable Long id) {
        return transferService.getTransfer(id);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("transfers/filter/{id}")
    public List<Transfer> getTransfersByAccountId(@PathVariable int id) {
        return transferService.getTransfersByAccountId(id);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("transfers")
    public List<Transfer> getAllTransfers() {
        return transferService.getAll();
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("users")
    public List<TenmoUser> getAllUsers() {
        return userAccountService.findAll();
    }
    @GetMapping("{id}/username")
    public String getUserNameByAccountId(@PathVariable int id) {
        return userAccountService.getUserNameByAccountId(id);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("{id}")
    public Account getAccountByAccountId(@PathVariable int id) {
        return userAccountService.getAccountByAccountId(id);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("user-account")
    public List<UserAccountDto> getAllUserAccount() {
        return userAccountService.findAllUserAccount();
    }
    @PreAuthorize("hasRole('USER')")
    @GetMapping("account-transfers/id/{userId}")
    public List<AccountTransferDto> getAllAccountTransfersByUser(@PathVariable int userId) {
        return accountTransferService.findAllAccountTransfersByUser(userId);
    }
    @PreAuthorize("hasRole('USER')")
    @GetMapping("account-transfers/username/{userName}")
    public List<AccountTransferDto> getAllAccountTransfersByUser(@PathVariable String userName) {
        return accountTransferService.findAllAccountTransfersByUser(userName);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("transfers/pending")
    public List<Transfer> getPendingTransfers(Principal principal) {
        System.out.println("test1");

        TenmoUser user = userAccountService.getUserByUsername(principal.getName());
        System.out.println((user.getUsername()));
        return transferService.getPendingTransfersByAccountId(user.getUserId());
    }
    @PreAuthorize("hasRole('USER')")
    @PostMapping("request")
        public TransferRequest createRequestTransfer(@RequestBody TransferRequest transfer) {
        return transferService.createRequestTransfer(transfer);
        }
}
