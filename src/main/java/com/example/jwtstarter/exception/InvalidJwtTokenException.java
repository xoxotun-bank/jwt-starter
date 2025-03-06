package com.example.jwtstarter.exception;

public class InvalidJwtTokenException extends RuntimeException {

    public InvalidJwtTokenException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public InvalidJwtTokenException(String msg) {
        super(msg);
    }

}
