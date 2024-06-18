package com.techelevator.tenmo.services;

import com.techelevator.tenmo.entities.Account;
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
import java.util.Optional;
import java.util.stream.Collectors;

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

    public Transfer getTransfer(Long id) {
        return transferRepository.findByTransferId(id).orElse(null);
    }

    public List<Transfer> getPendingTransfersByAccountId(int accountId) {
        List<Transfer> pendingTransfers = new ArrayList<>();
        System.out.println("Account ID: " + accountId);

        // Fetch all transfers and filter
        List<Transfer> allTransfers = transferRepository.findAll();

        // Stream API for filtering
        pendingTransfers = allTransfers.stream()
                .filter(transfer -> transfer.getAccountTo() == accountId && transfer.getStatusId() == 1)
                .peek(transfer -> {

                    // Fetch user information (assuming these methods are correct)
                    TenmoUser userFrom = tenmoUserRepository.findByUserId(accountRepository.findByAccountId(transfer.getAccountFrom()).getUserId());
                    TenmoUser userTo = tenmoUserRepository.findByUserId(accountRepository.findByAccountId(transfer.getAccountTo()).getUserId());

                    // Optional: Set usernames if needed
                    // transfer.setFromUsername(userFrom.getUsername());
                    // transfer.setToUsername(userTo.getUsername());
                })
                .collect(Collectors.toList());

        return pendingTransfers;
    }
    public TransferRequest createRequestTransfer(TransferRequest transferRequest,int accountId) {
        if (transferRequest.getAccountToId() ==0) {
            // Handle the case where accountToId is null, e.g., throw an exception or return an error response
            return null;
        }
        System.out.println("request");
        Transfer transfer = new Transfer();
        System.out.println(transferRequest.getAccountToId());
        transfer.setTransferId(10);
        transfer.setAccountTo(transferRequest.getAccountToId());
        transfer.setAccountFrom(accountId);
        transfer.setAmount(transferRequest.getAmount());
        transfer.setStatusId(1);
        transferRepository.save(transfer);
        // Set other properties as needed

        return transferRequest;
    }

    public boolean updateTransferStatus(Long transferId, String action) {
        Optional<Transfer> transferOpt = transferRepository.findById(transferId);
        if (transferOpt.isPresent()) {
            Transfer transfer = transferOpt.get();
            Account accountTo = accountRepository.findById(transfer.getAccountTo()).orElseThrow(() -> new RuntimeException("Account not found"));
            Account accountFrom = accountRepository.findById(transfer.getAccountFrom()).orElseThrow(() -> new RuntimeException("Account not found"));
            if ("approve".equalsIgnoreCase(action)) {
                transfer.setStatusId(2); // Assuming '2' is the status for approved
                accountTo.setBalance(accountTo.getBalance().add(transfer.getAmount()));
                accountFrom.setBalance(accountFrom.getBalance().subtract(transfer.getAmount()));
                accountRepository.save(accountTo);
                accountRepository.save(accountFrom);
            } else if ("reject".equalsIgnoreCase(action)) {
                transfer.setStatusId(3); // Assuming '3' is the status for rejected
            } else {
                return false;
            }
            transferRepository.save(transfer);
            return true;
        }
        return false;
    }
}
