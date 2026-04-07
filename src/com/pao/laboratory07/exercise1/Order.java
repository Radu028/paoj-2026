package com.pao.laboratory07.exercise1;

import com.pao.laboratory07.exercise1.exceptions.CannotCancelFinalOrderException;
import com.pao.laboratory07.exercise1.exceptions.CannotRevertInitialOrderStateException;
import com.pao.laboratory07.exercise1.exceptions.OrderIsAlreadyFinalException;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private OrderState state;
    private final List<OrderState> history = new ArrayList<>();

    public Order(OrderState initialState) {
        this.state = initialState;
    }

    public OrderState getState() {
        return state;
    }

    public void nextState() throws OrderIsAlreadyFinalException {
        if (state.isFinal()) {
            throw new OrderIsAlreadyFinalException("Order is already in a final state.");
        }
        history.add(state);
        state = state.next();
        System.out.println("Order state updated to: " + state);
    }

    public void cancel() throws CannotCancelFinalOrderException {
        if (state.isFinal()) {
            throw new CannotCancelFinalOrderException("Cannot cancel a final state order.");
        }
        history.add(state);
        state = OrderState.CANCELED;
        System.out.println("Order has been canceled.");
    }

    public void undoState() throws CannotRevertInitialOrderStateException {
        if (history.isEmpty()) {
            throw new CannotRevertInitialOrderStateException("Cannot undo the initial order state.");
        }
        state = history.remove(history.size() - 1);
        System.out.println("Order state reverted to: " + state);
    }
}
