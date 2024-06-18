package com.techelevator.tenmo.services;

import com.techelevator.tenmo.entities.TenmoUser;
import com.techelevator.tenmo.entities.Transfer;
import com.techelevator.tenmo.model.TransferRequest;
import com.techelevator.tenmo.repositories.AccountRepository;
import com.techelevator.tenmo.repositories.TenmoUserRepository;
import com.techelevator.tenmo.repositories.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransferService {
    @Autowired
    TransferRepository transferRepository;
    @Autowired
    private TenmoUserRepository tenmoUserRepository;

    @Autowired
    private AccountRepository accountRepository;

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

//    public Transfer sendTransfer(Transfer transfer) {
//        return transferRepository.save(transfer);
//    }

    public Transfer getTransfer(Long id) {
        return transferRepository.findByTransferId(id).orElse(null);
    }
    public List<Transfer> getPendingTransfersByAccountId(int accountId) {
        List<Transfer> pendingTransfers = new ArrayList<>();
        for (Transfer transfer : transferRepository.findAll()) {
            if ((transfer.getAccountFrom() == accountId || transfer.getAccountTo() == accountId) && transfer.getStatusId() == 1) {
                System.out.print(transfer.getStatus());
                TenmoUser userFrom = tenmoUserRepository.findByUserId(accountRepository.findByAccountId(transfer.getAccountFrom()).getUserId());
                TenmoUser userTo = tenmoUserRepository.findByUserId(accountRepository.findByAccountId(transfer.getAccountTo()).getUserId());
                //transfer.setFromUsername(userFrom.getUsername());
               // transfer.setToUsername(userTo.getUsername());
                pendingTransfers.add(transfer);
            }
        }
        return pendingTransfers;
    }
    public TransferRequest createRequestTransfer(TransferRequest transferRequest) {
        if (transferRequest.getAccountToId() ==0) {
            // Handle the case where accountToId is null, e.g., throw an exception or return an error response
            return null;
        }
        Transfer transfer = new Transfer();
        System.out.println(transferRequest.getAccountToId());
        transfer.setTransferId(10);
        transfer.setAccountTo(transferRequest.getAccountToId());
        transfer.setAmount(transferRequest.getAmount());
        transferRepository.save(transfer);
        // Set other properties as needed

        return transferRequest;
    }
}
