package com.techelevator.tenmo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.techelevator.tenmo.entities.Account;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    public Account findByAccountId(int id);
    public Account findByUserId(int id);

}
