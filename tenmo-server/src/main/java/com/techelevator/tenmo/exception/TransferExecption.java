package com.techelevator.tenmo.exception;

public class TransferExecption extends Exception{

    public TransferExecption() {
        super();
    }
    public TransferExecption(String message) {
        super(message);
    }
    public TransferExecption(String message, Exception cause) {
        super(message, cause);
    }

}
