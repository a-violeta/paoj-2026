package com.pao.project.bank.exception;

public class IllegalCurrencyException extends RuntimeException {
    public IllegalCurrencyException(String message) {
        super(message);
    }
}
