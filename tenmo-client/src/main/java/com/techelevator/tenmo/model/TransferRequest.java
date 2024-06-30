package com.techelevator.tenmo.model;

import java.math.BigDecimal;
public class TransferRequest {

    public TransferRequest() {

    }

    public TransferRequest(BigDecimal amount, int accountToId) {
        this.amount = amount;
        this.accountTo = accountToId;
    }
    private long transferId;
    private int transferTypeId;
    private int transferStatusId;
    private int accountFrom;
    private int accountTo;
    private BigDecimal amount;

    public long getTransferId() {
        return transferId;
    }

    public void setTransferId(long transferId) {
        this.transferId = transferId;
    }

    public int getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(int transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public int getTransferStatusId() {
        return transferStatusId;

    }

    public void setTransferStatusId(int transferStatusId) {
        this.transferStatusId = transferStatusId;
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
        switch (this.transferStatusId) {
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
        switch (this.transferTypeId) {
            case 1:
                return "Request";
            case 2:
                return "Send";
            default:
                return "Unknown Type";
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        TransferRequest other = (TransferRequest) obj;

        return this.transferId == other.transferId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode((int)transferId);
    }

}
