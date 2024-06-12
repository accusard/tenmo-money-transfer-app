package com.techelevator.tenmo.model;

import javax.persistence.*;
import java.math.BigDecimal;


@Entity
@Table(name = "transfer")
public class Transfer {
    public enum Type {
        Request(1),
        Send(2);

        private final int integer;

        Type(int type) {
            this.integer = type;
        }

        public int toInt() {
            return integer;
        }
    }


    public enum Status {
        Pending(1),
        Approved(2),
        Rejected(3);

        private final int integer;

        Status(int type) {
            this.integer = type;
        }

        public int toInt() {
            return integer;
        }
    }
    @Id
    @Column(name = "transfer_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transferId;

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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }




}