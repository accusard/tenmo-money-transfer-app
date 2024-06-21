package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.TransferExecption;

import java.math.BigDecimal;

public interface TransferDao {

    void transferTeBucks(int accountFromId, int accountToId, BigDecimal amount) throws TransferExecption;
}
