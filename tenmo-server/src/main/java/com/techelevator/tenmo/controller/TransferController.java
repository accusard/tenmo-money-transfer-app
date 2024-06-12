package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.services.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("transfers/")
public class TransferController {

    @Autowired
    TransferService transferService;

    @GetMapping("list")
    public List<Transfer> getTransfers() {
        return transferService.getTransfers();
    }

    @PostMapping("starttransfer")
    public Transfer startTransfer(@RequestBody Transfer transfer) {
        return transferService.startTransfer(transfer);
    }

    @GetMapping("")
    public Transfer getTransfer(Long id) {
        return transferService.getTransfer(id);
    }
}
