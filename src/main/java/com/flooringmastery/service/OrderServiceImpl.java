package com.flooringmastery.service;

import com.flooringmastery.dao.*;
import com.flooringmastery.dto.Order;
import com.flooringmastery.dto.Product;
import com.flooringmastery.dto.Tax;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class OrderServiceImpl implements OrderService{

    private OrderDao orderDao;
    private TaxDao taxDao;
    private ProductDao productDao;

    private static final BigDecimal MIN_AREA = new BigDecimal("100");

    public OrderServiceImpl(OrderDao orderDao,TaxDao tax, ProductDao product){
        this.orderDao = orderDao;
        this.taxDao = tax;
        this.productDao = product;

    }

    @Override
    public int getNextOrderNumber() {
        return 0;
    }

    @Override
    public void addOrder(Order order) throws OrderPersistenceException {
        validateOrderDate(order.getOrderDate());
        validateCustomerName(order.getCustomerName());
        validateState(order.getState());
        validateProductType(order.getProductType());
        validateArea(order.getArea());

        Tax tax =



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



    //Order Date must be in the future
    private LocalDate validateOrderDate(LocalDate date){
        if(!date.isAfter(LocalDate.now())){
            throw new InvalidOrderException("Order date must be at least 1 day in the future");
        }

        return date;
    }

    //May not be blank and is limited to characters [a-z][0-9] as well as periods and comma characters. "Acme, Inc." is a valid name.
    private String validateCustomerName(String customerName){

        if(!customerName.isEmpty() && !customerName.matches("(?i)[a-z0-9,.\\s]+")){
            throw new InvalidOrderException("Customer cannot be blank. may only contain alphanumerical characters as well as periods and command.  \"Acme, Inc.\" is a valid name.");
        }

        return customerName;
    }

    //Entered states must be checked against the tax file. If the state does not exist in the tax file, we cannot sell there.
    // If the tax file is modified to include the state, it should be allowed without changing the application code.
    private String validateState(String state){

        return state;
    }

    //Show a list of available products and pricing information to choose from.
    // Again, if a product is added to the file it should show up in the application without a code change.
    private String validateProductType (String productType){

        return productType;
    }

    //the area must be a positive decimal. Minimum order size is 100 sq ft.
    private BigDecimal validateArea(BigDecimal area){

        if (area == null || area.compareTo(MIN_AREA) < 0) {
            throw new InvalidOrderException("Order area must be at least 100 sq ft.");
        }
        return area;
    }


    private BigDecimal calculateMaterialCost(BigDecimal area, BigDecimal costPerSqrFt){


    }

    private BigDecimal calculateLabourCost(BigDecimal area, BigDecimal costPerSqrFt){


    }

    private BigDecimal calculateTax(BigDecimal area, BigDecimal costPerSqrFt){


    }

    private BigDecimal calculateTotal(BigDecimal area, BigDecimal costPerSqrFt){


    }
}
