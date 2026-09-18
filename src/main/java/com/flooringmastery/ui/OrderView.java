package com.flooringmastery.ui;

import com.flooringmastery.dto.Order;

import java.time.LocalDate;
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
        return io.readLocalDate("Enter date to search for orders (MMddyyyy)");
    }

    public void displayOrdersList(List<Order> orderList) {
        for (Order currentOrder : orderList) {
            io.print(currentOrder.toString());
        }
        io.readString("Please hit enter to continue.");
    }


    public void displayExitBanner() {
        io.print("Good Bye!!!");
    }

    public void displayUnknownCommandBanner() {
        io.print("Unknown Command!!!");
    }

    public void displayErrorMessage(String message){
        io.print(message);
    }

}
