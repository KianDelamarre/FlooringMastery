package com.flooringmastery.dao;


import com.flooringmastery.dto.Order;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.PrimitiveIterator;

public interface OrderDao {

    /**
     * Adds an order to persisted storage.
     *
     * @param order Order dto to persistence
     * @return the dto for the order that was peristed
     */
    public Order addOrder(Order order);

    /**
     *  Retrieves an order entry from persisted storage, returns null if order at
     *  specified date cannot be found
     *
     * @param date Date for the order to retrieve all entries for a given date
     * @param orderNumber order number to search within the date-filtered orders
     * @return the dto for the order that is being searched for
     */
    public Order getOrder(LocalDate date, int orderNumber);

    /**
     *  Edits details for an existing order
     *
     * @return the dto for the order that is being edited for
     */
    public Order editOrder(Order order);

    /**
     *  Retrieves all orders for a given date. returns null if none exist
     *
     * @param date Date for the order to retrieve all entries for a given date
     * @return a list of Order objects
     */
    public List<Order> getOrdersForDate(LocalDate date);

    /**
     *  Retrieves all orders  returns null if none exist
     *
     * @return a nested map with orders, sorted by Date
     */
    public Map<LocalDate, Map<Integer, Order>> getAllOrders();


    /**
     *
     * Remove an order for a specific date
     *
     * @param date
     * @param orderNumber
     * @return Order object for order removed
     */
    public Order removeOrder(LocalDate date, int orderNumber);

    public void exportAllDataToFile();


}
