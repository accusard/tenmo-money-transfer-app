package com.techelevator.tenmo.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "transfer")
public class TransferRequest {

    public TransferRequest() {

    }

    public TransferRequest(BigDecimal amount, int accountToId) {
        this.amount = amount;
        this.accountTo = accountToId;
    }

    @Id
    @Column(name = "transfer_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transferId;

    private Account account;

    @Column(name = "transfer_type_id")
    private int typeId;

    @Column(name = "transfer_status_id")
    private int statusId;

    @Column(name = "account_from")
    private int accountFrom;

    @Column(name = "account_to")
    private int accountTo;

    @Column(name = "amount")
    private BigDecimal amount;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

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

    public int getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(int accountFrom) {
        this.accountFrom = accountFrom;
    }

    public int getAccountTo() {
        return accountTo;
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
}
