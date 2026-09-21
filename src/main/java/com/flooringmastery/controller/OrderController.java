package com.flooringmastery.controller;

import com.flooringmastery.dao.OrderPersistenceException;
import com.flooringmastery.dto.Order;
import com.flooringmastery.dto.Product;
import com.flooringmastery.dto.Tax;
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

        if(orderList.isEmpty()){
            view.displayErrorMessage("No orders");
        }
        view.displayOrdersList(orderList);
    }

    private void addOrder() throws OrderPersistenceException {
        List<Product> products = service.getProducts();
        if(products.isEmpty()){
            view.displayErrorMessage("No available products");
            return;
        }
        List<Tax> taxes = service.getTaxes();
        if(taxes.isEmpty()){
            view.displayErrorMessage("No available states");
            return;
        }

        Order order = view.getNewOrderInfo(taxes, products);

        try {
            order = service.calculateFinalOrder(order);
        }
        catch (InvalidOrderException ex){
            view.displayErrorMessage(ex.getMessage());
            return; //return early since they inputted data that is not valid
        }
        boolean confirmed = view.getOrderConfirmation(order, "Are you happy with your order?");

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

        Order searchCriteria = view.getOrderDateAndNumber();

        Order existingOrder = service.getOrder(searchCriteria.getOrderDate(), searchCriteria.getOrderNumber()); //if can retrieve the full order with those details

        if(existingOrder == null){
            view.displayErrorMessage("No order data found for that date and order number.");
            return;
        }

        boolean confirmOrderToChange = view.getOrderConfirmation(existingOrder, "Is this the order you wish to edit?");

        if(!confirmOrderToChange){ //if they dont want to edit this order return early
            return;
        }

        List<Product> products = service.getProducts();
        if(products.isEmpty()){
            view.displayErrorMessage("No available products");
            return;
        }
        List<Tax> taxes = service.getTaxes();
        if(taxes.isEmpty()){
            view.displayErrorMessage("No available states");
            return;
        }


        Order editRequest = new Order(); //copying over values instead of straight copy since that would just make editRequest and Existing Order point to the same memory, meaning changes to one happens to both
        editRequest.setOrderDate(existingOrder.getOrderDate());
        editRequest.setOrderNumber(existingOrder.getOrderNumber());

        String newCustomerName = view.getNewCustomerName(existingOrder.getCustomerName());

        view.listStates(taxes);
        String newState = view.getNewState(existingOrder.getStateAbbr());

        view.listProducts(products);
        String newProductType = view.getNewProductType(existingOrder.getProductType());

        BigDecimal newArea = view.getNewArea(existingOrder.getArea());

        editRequest.setCustomerName(newCustomerName);
        editRequest.setStateAbbr(newState);
        editRequest.setProductType(newProductType);
        editRequest.setArea(newArea);

        Order calculatedOrder;
        try {
            calculatedOrder = service.calculateFinalOrder(editRequest);
        }
        catch (InvalidOrderException ex){
            view.displayErrorMessage(ex.getMessage());
            return; //return early since they inputted data that is not valid
        }

        boolean confirmChanges = view.getOrderChangeConfirmation(existingOrder,calculatedOrder, "Are you happy with the changes?");

        if(confirmChanges){
            service.editOrder(calculatedOrder);
        }
    }


    private void viewOrder() throws OrderPersistenceException {

    }

    private void removeOrder() throws OrderPersistenceException {
        Order searchCriteria = view.getOrderDateAndNumber();

        Order existingOrder = service.getOrder(searchCriteria.getOrderDate(), searchCriteria.getOrderNumber()); //if can retrieve the full order with those details

        if(existingOrder == null){
            view.displayErrorMessage("No order data found for that date and order number.");
            return;
        }

        boolean confirmOrderToChange = view.getOrderConfirmation(existingOrder, "Is this the order you wish to remove?");

        if(!confirmOrderToChange){ //if they dont want to edit this order return early
            return;
        }

        service.removeOrder(existingOrder);

        view.displayOrderRemovedBanner(existingOrder.getOrderNumber());

    }

    private void exportOrders() throws OrderPersistenceException {
        boolean confirmOExport = view.getExportConfirmation();

        if(!confirmOExport){
            return;
        }

        try {
            service.exportData();
        }
        catch (OrderPersistenceException ex){
            view.displayErrorMessage(ex.getMessage());
        }

        view.displayExportedDataBanner();

    }


    private void unknownCommand() {
        view.displayUnknownCommandBanner();
    }

    private void exitMessage() {
        view.displayExitBanner();
    }
}
