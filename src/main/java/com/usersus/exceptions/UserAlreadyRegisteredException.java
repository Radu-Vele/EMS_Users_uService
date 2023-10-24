package com.usersus.exceptions;

public class UserAlreadyRegisteredException extends Exception{
    public UserAlreadyRegisteredException(String message) {
        super(message);
    }
}
