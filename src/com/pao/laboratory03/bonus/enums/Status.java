package com.pao.laboratory03.bonus.enums;

public enum Status {

    TODO {
        public boolean canTransitionTo(Status next) {
            return next == IN_PROGRESS || next == CANCELLED;
        }
    },
    IN_PROGRESS {
        public boolean canTransitionTo(Status next) {
            return next == DONE || next == CANCELLED;
        }
    },
    DONE {
        public boolean canTransitionTo(Status next) {
            return false;
        }
    },
    CANCELLED {
        public boolean canTransitionTo(Status next) {
            return false;
        }
    };

    public abstract boolean canTransitionTo(Status next);
}