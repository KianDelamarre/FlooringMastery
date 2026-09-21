package com.flooringmastery.ui;

import com.flooringmastery.dto.Order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderView {

    private final UserIo io;

    public OrderView(UserIo io){
        this.io = io;
    }

    public int printMenuAndGetSelection() {
        io.print("Main Menu");
        io.print("1. Display Orders");
        io.print("2. Add an Order");
        io.print("3. Edit an Order");
        io.print("4. Remove an Order");
        io.print("5. Export all Order");
        io.print("6. Exit");

        return io.readInt("Please select from the above choices.", 1, 6);
    }

    public void displayViewOrdersBanner(){
        io.print("=== View Orders ===");
    }

    public LocalDate getOrderDate(){
        displayViewOrdersBanner();
        return io.readLocalDate("Enter date to search for orders (MMddyyyy)");
    }

    public void displayOrdersList(List<Order> orderList) {
        for (Order currentOrder : orderList) {
            displayOrderInfo(currentOrder);
        }
        io.readString("Please hit enter to continue.");
    }


    public void displayAddOrderBanner(){
        io.print("=== Add Order ===");
    }

    public Order getNewOrderInfo(){

        LocalDate orderDate = io.readLocalDate("Please enter order date (MMddyyyy): ");
        String customerName = io.readString("Your name: ");
        String state = io.readString("Your State abbrev (TX for texas): ");
        String productType = io.readString("Product type? ");
        BigDecimal area = io.readBigDecimal("Area (sqr ft) ");

        Order currentOrder = new Order();
        currentOrder.setOrderDate(orderDate);
        currentOrder.setCustomerName(customerName);
        currentOrder.setStateAbbr(state);
        currentOrder.setProductType(productType);
        currentOrder.setArea(area);

        return currentOrder;
    }

    public boolean getOrderConfirmationBanner(Order order, String prompt){
        io.print("Confirm order\n");

        displayOrderInfo(order);

        String confirmation = io.readString("\n" + prompt + " (Y\\N)", List.of("Y", "N"));

        return confirmation.equalsIgnoreCase("Y"); //return true if the user entered Y
    }



    public Order getOrderDateAndNumber(){

        LocalDate orderDate = io.readLocalDate("Please enter your order date (MMddyyyy): ");
        int orderNumber = io.readInt("Please enter your order number ");

        Order currentOrder = new Order();
        currentOrder.setOrderDate(orderDate);
        currentOrder.setOrderNumber(orderNumber);

        return currentOrder;
    }

    public String getNewCustomerName(String currentCustomerName) {
        String newCustomerName = io.readString("Enter new customer name (" + currentCustomerName + "): ");
        // If user hits enter (blank input), keep existing name
        if (newCustomerName.trim().isEmpty()) {
            return currentCustomerName;
        }
        return newCustomerName;
    }

    public String getNewState(String currentState) {
        String newState = io.readString("Enter new State (" + currentState + "): ");
        if (newState.trim().isEmpty()) {
            return currentState;
        }
        return newState;
    }

    public String getNewProductType(String currentProductType) {
        String newProductType = io.readString("Enter new product type (" + currentProductType + "): ");
        if (newProductType.trim().isEmpty()) {
            return currentProductType;
        }
        return newProductType;
    }

    public BigDecimal getNewArea(BigDecimal currentArea) {
        String newAreaInput = io.readString("Enter new area (" + currentArea + "): ");
        if (newAreaInput.trim().isEmpty()) {
            return currentArea;
        }
        // Parse the input String into a BigDecimal
        return new BigDecimal(newAreaInput.trim());
    }

    public void displayOrderRemovedBanner(int orderNumber){
        io.print("=== Order #" +orderNumber+  " succesfully removed ===");
    }



    public void displayOrderInfo(Order order){
        io.print("================="+
                "\nOrder Number: " + order.getOrderNumber() +
                "\nCustomer name: " + order.getCustomerName()+
                "\nOrder date: " + order.getOrderDate()+
                "\nState: "+ order.getStateAbbr()+
                "\nTax Rate: "+order.getTaxRate()+
                "\nProduct Type: "+order.getProductType()+
                "\nArea: "+order.getArea()+
                "\nCost Per Sqr Ft: "+order.getCostPerSquareFoot()+
                "\nLabour Cost per sqr Ft: "+order.getLabourCostPerSquareFoot()+
                "\nMaterial Cost: "+order.getMaterialCost()+
                "\nLabour Cost: "+order.getLabourCost()+
                "\nTax: "+order.getTax()+
                "\nTotal: "+order.getTotal()+
                "\n=================");
    }


    public void displayExitBanner() {
        io.print("Good Bye!!!");
    }

    public void displayUnknownCommandBanner() {
        io.print("Unknown Command!!!");
    }

    public void displayErrorMessage(String message){
        io.print(message + "\n");
    }

}
