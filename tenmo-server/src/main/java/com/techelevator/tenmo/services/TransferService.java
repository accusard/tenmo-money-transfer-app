package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.repositories.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransferService {
    @Autowired
    TransferRepository transferRepository;

    public TransferService() {}

    public List<Transfer> getTransfers() {
        return transferRepository.findAll();
    }

    public Transfer startTransfer(Transfer transfer) {
        return transferRepository.save(transfer);
    }

    public Transfer getTransfer(Long id) {
        return transferRepository.findByTransferId(id).orElse(null);
    }
}
