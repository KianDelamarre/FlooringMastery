package com.flooringmastery.controller;

import com.flooringmastery.dao.OrderPersistenceException;
import com.flooringmastery.dto.Order;
import com.flooringmastery.service.InvalidOrderException;
import com.flooringmastery.service.OrderService;
import com.flooringmastery.service.OrderServiceImpl;
import com.flooringmastery.ui.OrderView;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class OrderController {

    private final OrderView view;

    private OrderService service;

    public OrderController(OrderView view, OrderService service){
        this.view = view;
        this.service = service;
    }

    public void run() throws Exception{
        boolean keepGoing = true;
        int menuSelection = 0;
        while (keepGoing) {

            menuSelection = getMenuSelection();

            switch (menuSelection) {
                case 1:
                    listOrders();
                    break;
                case 2:
                    addOrder();
                    break;
                case 3:
                    editOrder();
                    break;
                case 4:
                    removeOrder();
                    break;
                case 5:
                    exportOrders();
                    break;
                case 6:
                    keepGoing = false;
                    break;    
                default:
                    unknownCommand();
            }

        }
        exitMessage();
    }

    private int getMenuSelection() {
        return view.printMenuAndGetSelection();
    }


    private void listOrders() throws OrderPersistenceException {
        LocalDate orderDate = view.getOrderDate();

        List<Order> orderList = service.getOrdersForDate(orderDate);

        view.displayOrdersList(orderList);
    }

    private void addOrder() throws OrderPersistenceException {
        Order order = view.getNewOrderInfo();

        try {
            order = service.calculateFinalOrder(order);
        }
        catch (InvalidOrderException ex){
            view.displayErrorMessage(ex.getMessage());
            return; //return early since they inputted data that is not valid
        }
        boolean confirmed = view.getOrderConfirmationBanner(order, "Are you happy with your order?");

        if(confirmed){
            service.addOrder(order);
        }
    }

    //1. get order using date and number
    //2. display order details to get confirmation they want to edit this order
    //3. ask for new order details
    //4. recalculate and display new order details for confirmation
    //5. edit the order
    private void editOrder() throws OrderPersistenceException {
        Order orderToChange = view.getOrderDateAndNumber();

        orderToChange = service.getOrder(orderToChange.getOrderDate(), orderToChange.getOrderNumber()); //if can retrieve the full order with those details

        if(orderToChange == null){
            view.displayErrorMessage("No order data found for that date and order number.");
        }

        boolean confirmOrderToChange = view.getOrderConfirmationBanner(orderToChange, "Is this the order you wish to edit?");

        if(!confirmOrderToChange){ //if they dont want to edit this order return early
            return;
        }

        String newCustomerName = view.getNewCustomerName(orderToChange.getCustomerName());
        String newState = view.getNewState(orderToChange.getStateAbbr());
        String newProductType = view.getNewProductType(orderToChange.getProductType());
        BigDecimal newArea = view.getNewArea(orderToChange.getArea());

        orderToChange.setCustomerName(newCustomerName);
        orderToChange.setStateAbbr(newState);
        orderToChange.setProductType(newProductType);
        orderToChange.setArea(newArea);

        try {
            orderToChange = service.calculateFinalOrder(orderToChange);
        }
        catch (InvalidOrderException ex){
            view.displayErrorMessage(ex.getMessage());
            return; //return early since they inputted data that is not valid
        }

        boolean confirmChanges = view.getOrderConfirmationBanner(orderToChange, "Are you happy with the changes?");

        if(confirmChanges){
            service.editOrder(orderToChange);
        }
    }


    private void viewOrder() throws OrderPersistenceException {

    }

    private void removeOrder() throws OrderPersistenceException {

    }

    private void exportOrders() throws OrderPersistenceException {

    }


    private void unknownCommand() {
        view.displayUnknownCommandBanner();
    }

    private void exitMessage() {
        view.displayExitBanner();
    }
}
