package com.flooringmastery.service;

import com.flooringmastery.dao.OrderDao;
import com.flooringmastery.dao.OrderPersistenceException;
import com.flooringmastery.dto.Order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDaoStubImpl implements OrderDao {
    public Order onlyOrder;


    public OrderDaoStubImpl(){
        onlyOrder = new Order();
        onlyOrder.setOrderNumber(0);
        onlyOrder.setCustomerName("Ada Lovelace");
        onlyOrder.setOrderDate(LocalDate.parse("2025-08-21"));
        onlyOrder.setStateAbbr("CA");
        onlyOrder.setTaxRate(new BigDecimal("25.00"));
        onlyOrder.setProductType("Tile");
        onlyOrder.setArea(new BigDecimal("249.00"));
        onlyOrder.setCostPerSquareFoot(new BigDecimal("3.50"));
        onlyOrder.setLabourCostPerSquareFoot(new BigDecimal("4.15"));
        onlyOrder.setMaterialCost(new BigDecimal("871.50"));
        onlyOrder.setLabourCost(new BigDecimal("1033.35"));
        onlyOrder.setTax(new BigDecimal("476.21"));
        onlyOrder.setTotal(new BigDecimal("2381.06"));
    }

    public OrderDaoStubImpl(Order testOrder){this.onlyOrder = testOrder;}

    @Override
    public Order addOrder(Order order) throws OrderPersistenceException {
        return order;
    }

    @Override
    public Order getOrder(LocalDate date, int orderNumber) {
        if( (date.equals(onlyOrder.getOrderDate())) && (orderNumber==onlyOrder.getOrderNumber()) )
            return this.onlyOrder;

        return null;
    }

    @Override
    public Order editOrder(Order order) {
        if (order.getOrderNumber() == onlyOrder.getOrderNumber()) {
            return onlyOrder;
        }
        return null;
    }

    @Override
    public List<Order> getOrdersForDate(LocalDate date) {
        if(date.equals(onlyOrder.getOrderDate())) {
            return new ArrayList<>(List.of(this.onlyOrder));
        }

        return null;
    }

    @Override
    public Map<LocalDate, Map<Integer, Order>> getAllOrders() {
        Map<LocalDate, Map<Integer, Order>> allOrders = new HashMap<>();
        Map<Integer, Order> order = new HashMap<>();

        order.put(this.onlyOrder.getOrderNumber(), this.onlyOrder);

        allOrders.put(this.onlyOrder.getOrderDate(), order);

        return allOrders;
    }

    @Override
    public Order removeOrder(LocalDate date, int orderNumber) {
        if( (date.equals(onlyOrder.getOrderDate())) && (orderNumber==onlyOrder.getOrderNumber()) )
            return this.onlyOrder;

        return null;
    }
}
