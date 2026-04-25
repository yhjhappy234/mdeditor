package com.yhj.mdeditor.exception;

/**
 * User Already Exists Exception
 */
public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}