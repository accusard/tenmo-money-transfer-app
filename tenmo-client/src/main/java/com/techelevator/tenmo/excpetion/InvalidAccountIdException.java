package com.techelevator.tenmo.excpetion;

public class InvalidAccountIdException extends Exception{

    public InvalidAccountIdException() {}
    public InvalidAccountIdException(String message) {
        super(message);
    }
}