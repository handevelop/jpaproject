package com.example.jpa.pay.exception;

public class PasswordMissMatchException extends RuntimeException{
    public PasswordMissMatchException(String message){
        super(message);
    }
}
