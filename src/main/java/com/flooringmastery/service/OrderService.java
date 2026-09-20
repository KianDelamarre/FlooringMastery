package com.flooringmastery.service;

import com.flooringmastery.dao.OrderPersistenceException;
import com.flooringmastery.dto.Order;
import com.flooringmastery.dto.Product;
import com.flooringmastery.dto.Tax;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {

    int getNextOrderNumber();

    Order addOrder(Order order) throws
            OrderPersistenceException;

    Order calculateFinalOrder(Order order) throws
            OrderPersistenceException;

    Order getOrder(LocalDate date, int orderNumber);

    Order editOrder(Order order);

    List<Order> getOrdersForDate(LocalDate date) throws
            OrderPersistenceException;

    Order removeOrder (LocalDate date, int OrderNumber);

    void ExportData();

    List<Tax> getTaxes();
    List<Product> getProducts();



}
