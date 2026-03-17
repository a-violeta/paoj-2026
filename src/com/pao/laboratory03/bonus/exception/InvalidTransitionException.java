package com.pao.laboratory03.bonus.exception;

import com.pao.laboratory03.bonus.enums.Status;

public class InvalidTransitionException extends RuntimeException {

    private Status from;
    private Status to;

    public InvalidTransitionException(Status from, Status to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public String getMessage() {
        return "Nu se poate trece din " + from + " in " + to;
    }
}