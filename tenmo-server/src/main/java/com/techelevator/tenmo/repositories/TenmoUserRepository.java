package com.techelevator.tenmo.repositories;

import com.techelevator.tenmo.entities.TenmoUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TenmoUserRepository extends JpaRepository<TenmoUser, Integer> {
    public TenmoUser findByUserId(int id);
    public TenmoUser findByUserName(String userName);
}
