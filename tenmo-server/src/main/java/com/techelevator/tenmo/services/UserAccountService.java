package com.techelevator.tenmo.services;

import com.techelevator.tenmo.entities.Account;
import com.techelevator.tenmo.entities.TenmoUser;
import com.techelevator.tenmo.model.UserAccountDto;
import com.techelevator.tenmo.repositories.AccountRepository;
import com.techelevator.tenmo.repositories.TenmoUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserAccountService {
    @Autowired
    TenmoUserRepository tenmoUserRepository;
    @Autowired
    AccountRepository accountRepository;

    public UserAccountService() {}

    public String getUserNameByAccountId(int accountId) {
        Account account = accountRepository.findByAccountId(accountId);
        return tenmoUserRepository.findByUserId(account.getUserId()).getUserName();
    }

    public Account getAccountByAccountId(int id) {
        return accountRepository.findByAccountId(id);
    }

    public List<TenmoUser> findAll() {
        return tenmoUserRepository.findAll();
    }

    public List<UserAccountDto> findAllUserAccount() {
        return accountRepository.findAll().stream()
                .map(account -> new UserAccountDto(
                        account.getUserId(),
                        account.getTenmoUser().getUserName(),
                        account.getAccountId(),
                        account.getBalance()))
                .collect(Collectors.toList());
    }
}
