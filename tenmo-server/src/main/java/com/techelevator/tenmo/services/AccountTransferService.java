package com.techelevator.tenmo.services;

import com.techelevator.tenmo.entities.Account;
import com.techelevator.tenmo.model.AccountTransferDto;
import com.techelevator.tenmo.repositories.AccountRepository;
import com.techelevator.tenmo.repositories.TenmoUserRepository;
import com.techelevator.tenmo.repositories.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountTransferService {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    TransferRepository transferRepository;
    @Autowired
    TenmoUserRepository tenmoUserRepository;

    public List<AccountTransferDto> findAllAccountTransfersByUser(int userId) {
        Account userAccount = accountRepository.findByUserId(userId);
        int accountId = userAccount.getAccountId();

        return (transferRepository.findAll().stream()
                .filter(transfer -> transfer.getAccountTo() == accountId || transfer.getAccountFrom() == accountId )
                .map(transfer -> new AccountTransferDto(
                        userAccount.getUserId(),
                        userAccount.getAccountId(),
                        userAccount.getBalance(),
                        transfer.getTransferId(),
                        transfer.getTransferTypeId(),
                        transfer.getTransferStatusId(),
                        transfer.getAccountFrom(),
                        transfer.getAccountTo(),
                        transfer.getAmount()))
                .collect(Collectors.toList()));

        //
    }

    public List<AccountTransferDto> findAllAccountTransfersByUser(String userName) {
        int userId = tenmoUserRepository.findByUserName(userName).getUserId();
        return findAllAccountTransfersByUser(userId);
    }
}
