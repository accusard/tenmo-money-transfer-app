package com.techelevator.tenmo.services;

import com.techelevator.tenmo.entities.Transfer;
import com.techelevator.tenmo.repositories.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransferService {
    @Autowired
    TransferRepository transferRepository;

    public TransferService() {}

    public List<Transfer> getAll() {
        return transferRepository.findAll();
    }

    public List<Transfer> getTransfersByAccountId(int id) {
        List<Transfer> transfersById = new ArrayList<>();

        for(Transfer t : transferRepository.findAll()) {
            if(t.getAccountFrom() == id || t.getAccountTo() == id) {
                transfersById.add(t);
            }
        }
        return transfersById;
    }

    public Transfer getTransfer(Long id) {
        return transferRepository.findByTransferId(id).orElse(null);
    }
}
