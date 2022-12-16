package fr.unice.polytech.cod.interfaces;

import fr.unice.polytech.cod.order.Bill;
import fr.unice.polytech.cod.order.Order;
import fr.unice.polytech.cod.order.OrderState;
import fr.unice.polytech.cod.store.Store;

import java.util.List;

public interface OrderStatesAction {
    // Setter
    void cancelOrder(Order order);

    /**
     * Set the state of the order to the given OrderState.
     * If the state is set to READY run the UpdatableObject thread.
     *
     * @param order - The order
     * @param newState -  The new state of the order.
     */
    void updateState(Order order, OrderState newState);



    public void retrieveOrder(List<Order> orderList,Bill bill) throws Exception;
    OrderState getOrderState(Order order);

}
