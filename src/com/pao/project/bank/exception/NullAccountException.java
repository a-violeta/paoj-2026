package com.pao.project.bank.exception;

public class NullAccountException extends RuntimeException {
    public NullAccountException(String message) {
        super(message);
    }
}
