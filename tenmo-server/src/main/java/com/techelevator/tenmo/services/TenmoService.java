package com.techelevator.tenmo.services;

import com.techelevator.tenmo.entities.Account;
import com.techelevator.tenmo.entities.TenmoUser;
import com.techelevator.tenmo.repositories.AccountRepository;
import com.techelevator.tenmo.repositories.TenmoUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TenmoService {
    @Autowired
    TenmoUserRepository tenmoUserRepository;
    @Autowired
    AccountRepository accountRepository;

    public TenmoService() {}

    public String getUserNameByAccountNumber(int accountId) {
        Account account = accountRepository.findByAccountId(accountId);
        return tenmoUserRepository.findByUserId(account.getUserId()).getUserName();
    }

    public Account getAccountById(int id) {
        return accountRepository.findByAccountId(id);
    }
    public List<TenmoUser> findAll() {
        return tenmoUserRepository.findAll();
    }
}
