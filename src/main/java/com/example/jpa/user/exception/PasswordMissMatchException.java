package com.example.jpa.user.exception;

public class PasswordMissMatchException extends RuntimeException{
    public PasswordMissMatchException(String message){
        super(message);
    }
}
