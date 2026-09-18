package com.flooringmastery.controller;

import com.flooringmastery.dao.OrderPersistenceException;
import com.flooringmastery.dto.Order;
import com.flooringmastery.service.OrderService;
import com.flooringmastery.service.OrderServiceImpl;
import com.flooringmastery.ui.OrderView;

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
                    viewOrder();
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
