package com.revature.ers.common.datasource.exceptions;

public class AuthenticationException extends RuntimeException{

    public AuthenticationException() {
        super("Could not find a user account with the provided credentials!");
    }
}
