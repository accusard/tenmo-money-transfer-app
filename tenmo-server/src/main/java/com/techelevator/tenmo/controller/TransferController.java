package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.entities.Transfer;
import com.techelevator.tenmo.services.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Uses Spring Data JPA pattern to access database.
 * Provides boilerplate code through repositories
 * to perform common CRUD operations
 */
@RestController
@RequestMapping("account/transfers/")
public class TransferController {

    @Autowired
    TransferService transferService;

    @GetMapping("")
    public List<Transfer> getTransfers() {
        return transferService.getTransfers();
    }

//    @PostMapping("send")
//    public Transfer sendTransfer(@RequestBody Transfer transfer) {
//        return transferService.sendTransfer(transfer);
//    }

    @GetMapping("{id}")
    public Transfer getTransfer(@PathVariable Long id) {
        return transferService.getTransfer(id);
    }
}
