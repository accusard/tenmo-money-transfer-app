package com.techelevator.tenmo.entities;

import javax.persistence.*;
import java.math.BigDecimal;


@Entity
@Table(name = "transfer")
public class Transfer {

//    public final int STATUS_PENDING = 1;
//    public final int STATUS_APPROVED = 2;
//    public final int STATUS_REJECTED = 3;
//    public final int TYPE_REQUEST = 1;
//    public final int TYPE_SEND = 2;

    @Id
    @Column(name = "transfer_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transferId;

    @Column(name = "transfer_type_id")
    private int transferTypeId;

    @Column(name = "transfer_status_id")
    private int transferStatusId;

    @Column(name = "account_from")
    private int accountFrom;

    @Column(name = "account_to")
    private int accountTo;

    @Column(name = "amount")
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "account_from", insertable = false, updatable = false)
    private Account accountFromEntity;

    @ManyToOne
    @JoinColumn(name = "account_to", insertable = false, updatable = false)
    private Account accountToEntity;

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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public String getStatus() {
        return TransferStatus.fromId(this.transferStatusId).getDescription();
    }

}
