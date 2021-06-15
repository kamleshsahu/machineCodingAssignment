package com.assignment.suprdaily.service;

import com.assignment.suprdaily.Entity.OrderRequest;
import com.assignment.suprdaily.exception.OrderReservationException;

public interface OrderFulfilmentService {
    /**
     * Takes an order request and checks if we can fulfil that order based
     * on various criterias mentioned in the problem
     *
     * @param orderRequest Order Request that customer is trying to order.
     * @return true if we can take this order, false if we cannot.
     */
    Boolean canFulfilOrder(OrderRequest orderRequest);
    /**
     * Reserves the items in the order request. This is usually called by client
     * after checking the above method. If an order is reserved,
     * that inventory is not available for any other order.
     *
     * @param orderRequest Order Request that customer is trying to order.
     * @throws OrderReservationException if we cannot take that order.
     */
    void reserveOrder(OrderRequest orderRequest) throws OrderReservationException;
}
