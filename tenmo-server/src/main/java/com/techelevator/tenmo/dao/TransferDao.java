package com.techelevator.tenmo.dao;

import java.math.BigDecimal;

public interface TransferDao {

    void transferTeBucks(int accountFromId, int accountToId, BigDecimal amount);
}
