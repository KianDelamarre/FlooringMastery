package com.flooringmastery;

import com.flooringmastery.controller.OrderController;
import com.flooringmastery.dao.*;
import com.flooringmastery.dto.Order;
import com.flooringmastery.service.OrderService;
import com.flooringmastery.service.OrderServiceImpl;
import com.flooringmastery.ui.OrderView;
import com.flooringmastery.ui.UserIo;
import com.flooringmastery.ui.UserIoConsoleImpl;

public class App {
    static void main(String[] args) throws Exception{
        UserIo io = new UserIoConsoleImpl();
        OrderView view = new OrderView(io);

        OrderDao OrderDao = new OrderDaoFileImpl("::", "Orders", "Data");
        TaxDao taxDao = new TaxDaoFileImpl("::", "Data/tax.txt");
        ProductDao productDao = new ProductDaoFileImpl("::", "Date/product.txt");

        OrderService service = new OrderServiceImpl(OrderDao, taxDao, productDao);
        OrderController controller = new OrderController(view, service);
        controller.run();
    }
}
