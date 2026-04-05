package com.pao.laboratory07.exercise1;

import com.pao.laboratory07.exercise1.exceptions.CannotCancelFinalOrderException;
import com.pao.laboratory07.exercise1.exceptions.CannotRevertInitialOrderStateException;
import com.pao.laboratory07.exercise1.exceptions.OrderIsAlreadyFinalException;

import java.util.Stack;

public class Order {

    private OrderState currentState;
    private final Stack<OrderState> history = new Stack<>();

    public Order(OrderState initialState) {
        this.currentState = initialState;
    }

    public OrderState getCurrentState() {
        return currentState;
    }

    private boolean isFinal() {
        return currentState == OrderState.DELIVERED || currentState == OrderState.CANCELED;
    }

    public void nextState() throws OrderIsAlreadyFinalException {
        if (isFinal()) {
            throw new OrderIsAlreadyFinalException("Final state");
        }

        history.push(currentState);

        switch (currentState) {
            case PLACED -> currentState = OrderState.PROCESSED;
            case PROCESSED -> currentState = OrderState.SHIPPED;
            case SHIPPED -> currentState = OrderState.DELIVERED;
        }

        System.out.println("Order state updated to: " + currentState);
    }

    public void cancel() throws CannotCancelFinalOrderException {
        if (isFinal()) {
            throw new CannotCancelFinalOrderException("Already final");
        }

        history.push(currentState);
        currentState = OrderState.CANCELED;
        System.out.println("Order has been canceled.");
    }

    public void undoState() throws CannotRevertInitialOrderStateException {
        if (history.isEmpty()) {
            throw new CannotRevertInitialOrderStateException("No previous state");
        }

        currentState = history.pop();
        System.out.println("Order state reverted to: " + currentState);
    }
}
