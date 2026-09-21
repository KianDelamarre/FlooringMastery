package com.flooringmastery.ui;

import com.flooringmastery.dto.Order;
import com.flooringmastery.dto.Product;
import com.flooringmastery.dto.Tax;

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
        io.print("\nMain Menu");
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

    public Order getNewOrderInfo(List<Tax> availableStates, List<Product> availableProducts){

        LocalDate orderDate = io.readLocalDate("Please enter order date (MMddyyyy): ");
        String customerName = io.readString("Your name: ");
        listStates(availableStates);
        String state = io.readString("Your State abbrev (TX for texas): ");
        listProducts(availableProducts);
        String productType = io.readString("Product type? ");
        BigDecimal area = io.readBigDecimal("Area (sqr ft, min 100) ");

        Order currentOrder = new Order();
        currentOrder.setOrderDate(orderDate);
        currentOrder.setCustomerName(customerName);
        currentOrder.setStateAbbr(state);
        currentOrder.setProductType(productType);
        currentOrder.setArea(area);

        return currentOrder;
    }

    public void listStates(List<Tax> taxes){
        io.print("Available States and Tax Rates");
        io.print("State | Abbr | Tax rate");
        taxes.forEach(tax -> io.print(tax.getState() + " | "+ tax.getStateAbr() +  " | "+ tax.getTaxRate()));
    }

    public void listProducts(List<Product> products){
        io.print("Available products");
        io.print("product | $/ft^2 | Labour $/ft^2");
        products.forEach(product -> io.print(product.getProductType() + " | "+ product.getCostPerSquareFoot() + " | "+ product.getLabourCostPerSquareFoot()));
    }

    public boolean getOrderConfirmation(Order order, String prompt){
        io.print("Confirm order\n");

        displayOrderInfo(order);

        String confirmation = io.readString("\n" + prompt + " (Y\\N)", List.of("Y", "N"));

        return confirmation.equalsIgnoreCase("Y"); //return true if the user entered Y
    }

    public boolean getOrderChangeConfirmation(Order oldOrder,Order newOrder, String prompt){
        io.print("Confirm order changes\n");

        displayOrderInfoChanges(oldOrder, newOrder);

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
        BigDecimal newAreaInput = io.readBigDecimal("Enter new area (" + currentArea + "): ", true);
        if (newAreaInput==null) {
            return currentArea;
        }
        // Parse the input String into a BigDecimal
        return newAreaInput;
    }

    public void displayOrderRemovedBanner(int orderNumber){
        io.print("=== Order #" +orderNumber+  " succesfully removed ===");
        io.readString("Please hit enter to continue.");
    }

    public boolean getExportConfirmation(){
        String confirmation = io.readString("Confirm export all data (Y\\N)", List.of("Y", "N"));

        return confirmation.equalsIgnoreCase("Y"); //return true if the user entered Y
    }

    public String newLine(){
        return "\n";
    }



    public void displayOrderInfo(Order order){
        io.print("\n================="+
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

    public void displayOrderInfoChanges(Order oldOrder, Order newOrder){
        io.print("\n================="+
                "\nOrder Number: " + oldOrder.getOrderNumber() + "-->" + newOrder.getOrderNumber()+
                "\nCustomer name: " + oldOrder.getCustomerName()+ "-->" + newOrder.getCustomerName()+
                "\nOrder date: " + oldOrder.getOrderDate()+ "-->" + newOrder.getOrderDate()+
                "\nState: "+ oldOrder.getStateAbbr()+ "-->" + newOrder.getStateAbbr()+
                "\nTax Rate: "+oldOrder.getTaxRate()+ "-->" + newOrder.getTaxRate()+
                "\nProduct Type: "+oldOrder.getProductType()+ "-->" + newOrder.getProductType()+
                "\nArea: "+oldOrder.getArea()+ "-->" + newOrder.getArea()+
                "\nCost Per Sqr Ft: "+oldOrder.getCostPerSquareFoot()+ "-->" + newOrder.getCostPerSquareFoot()+
                "\nLabour Cost per sqr Ft: "+oldOrder.getLabourCostPerSquareFoot()+ "-->" + newOrder.getLabourCostPerSquareFoot()+
                "\nMaterial Cost: "+oldOrder.getMaterialCost()+ "-->" + newOrder.getMaterialCost()+
                "\nLabour Cost: "+oldOrder.getLabourCost()+ "-->" + newOrder.getLabourCost()+
                "\nTax: "+oldOrder.getTax()+ "-->" + newOrder.getTax()+
                "\nTotal: "+oldOrder.getTotal()+ "-->" + newOrder.getTotal()+
                "\n=================");
    }

    public void displayExportedDataBanner(){
        io.print("=== Data successfully exported to Backup/DataExport.txt ===");
        io.readString("Please hit enter to continue.");
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
