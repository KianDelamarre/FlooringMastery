package com.flooringmastery.service;

import com.flooringmastery.dao.OrderDao;
import com.flooringmastery.dao.OrderPersistenceException;
import com.flooringmastery.dto.Order;
import com.flooringmastery.dto.Product;
import com.flooringmastery.dto.Tax;

import java.time.LocalDate;
import java.util.List;

public class OrderServiceImpl implements OrderService{

    private OrderDao orderDao;

    public OrderServiceImpl(OrderDao orderDao){
        this.orderDao = orderDao;

    }

    @Override
    public int getNextOrderNumber() {
        return 0;
    }

    @Override
    public void addOrder(Order order) throws OrderPersistenceException {

    }

    @Override
    public Order getOrder(LocalDate date, int orderNumber) {
        return null;
    }

    @Override
    public Order editOrder(LocalDate orderDate, int orderNumber) {
        return null;
    }

    @Override
    public List<Order> getOrdersForDate(LocalDate date) throws OrderPersistenceException {
        return orderDao.getOrdersForDate(date);
    }

    @Override
    public Order removeOrder(LocalDate date, int OrderNumber) {
        return null;
    }

    @Override
    public void ExportData() {

    }

    @Override
    public List<Tax> getTaxes() {
        return List.of();
    }

    @Override
    public List<Product> getProducts() {
        return List.of();
    }
}
