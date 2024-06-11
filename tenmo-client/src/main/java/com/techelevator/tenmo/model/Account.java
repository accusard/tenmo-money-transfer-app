package com.techelevator.tenmo.model;

import java.util.Objects;

public class Account {
    private String name;
    private int id;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAccountId() {
        return id;
    }

    public void setAccountId(int accountId) {
        this.id = accountId;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof User) {
            User otherUser = (User) other;
            return otherUser.getId() == id
                && otherUser.getUsername().equals(name);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id);
    }
}
