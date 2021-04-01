package com.example.jpa.user.model;

public enum UserStatus {

    None, Using, Stop;

    int value;

    UserStatus() {

    }

    public int value() {
        return this.value;
    }

}
