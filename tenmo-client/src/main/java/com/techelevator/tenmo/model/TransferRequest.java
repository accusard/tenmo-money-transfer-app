package com.techelevator.tenmo.model;

import java.math.BigDecimal;
public class TransferRequest {

    public TransferRequest() {

    }

    public TransferRequest(BigDecimal amount, int accountToId) {
        this.amount = amount;
        this.accountTo = accountToId;
    }
    private Long transferId;
    private int typeId;
    private int statusId;
    private int accountFrom;
    private int accountTo;
    private BigDecimal amount;

    public long getTransferId() {
        return transferId;
    }

    public void setTransferId(long transferId) {
        this.transferId = transferId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getStatusId() {
        return statusId;

    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public int getAccountFromId() {
        return accountFrom;
    }

    public void setAccountFrom(int accountFrom) {
        this.accountFrom = accountFrom;
    }

    public void setAccountTo(int accountTo) {
        this.accountTo = accountTo;
    }
    public int getAccountToId() {
        return accountTo;
    }

    public void setAccountToId(int accountToId) {
        this.accountTo = accountToId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getStatus() {
        switch (this.statusId) {
            case 1:
                return "Pending";
            case 2:
                return "Approved";
            case 3:
                return "Rejected";
            default:
                return "Unknown status";
        }
    }

    public String getType() {
        switch (this.typeId) {
            case 1:
                return "Request";
            case 2:
                return "Send";
            default:
                return "Unknown Type";
        }
    }
}
