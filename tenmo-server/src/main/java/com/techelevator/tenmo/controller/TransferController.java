package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.entities.Account;
import com.techelevator.tenmo.entities.TenmoUser;
import com.techelevator.tenmo.entities.Transfer;
import com.techelevator.tenmo.services.TenmoService;
import com.techelevator.tenmo.services.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Uses Spring Data JPA pattern to access database.
 * Provides boilerplate code through repositories
 * to perform common CRUD operations
 */
@RestController
@RequestMapping("account/")
public class TransferController {

    @Autowired
    TransferService transferService;
    @Autowired
    TenmoService tenmoService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping("transfers/")
    public List<Transfer> getTransfers() {
        return transferService.getTransfers();
    }

//    @PostMapping("send")
//    public Transfer sendTransfer(@RequestBody Transfer transfer) {
//        return transferService.sendTransfer(transfer);
//    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("transfers/{id}")
    public Transfer getTransfer(@PathVariable Long id) {
        return transferService.getTransfer(id);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("{id}/username")
    public String getUserNameByAccountId(@PathVariable int id) {
        return tenmoService.getUserNameByAccountNumber(id);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("{id}")
    public Account getAccountByAccountId(@PathVariable int id) {
        return tenmoService.getAccountById(id);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("users")
    public List<TenmoUser> getAllUsers() {
        return tenmoService.findAll();
    }
}
