package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransferContoller {
    private TransferDao transferDao;

    public TransferContoller(TransferDao transferDao) {
        this.transferDao = transferDao;
    }


}
