package com.techelevator.tenmo.entities;

public enum TransferStatus {
    PENDING(1, "Pending"),
    APPROVED(2, "Approved"),
    REJECTED(3, "Rejected");

    private final int id;
    private final String description;

    TransferStatus(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public static TransferStatus fromId(int id) {
        for (TransferStatus status : values()) {
            if (status.getId() == id) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid status ID");
    }
}
