package com.flooringmastery;

import com.flooringmastery.controller.OrderController;
import com.flooringmastery.dao.OrderDao;
import com.flooringmastery.dao.OrderDaoFileImpl;
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
        OrderDao dao = new OrderDaoFileImpl("::", "Orders", "Data");

        OrderService service = new OrderServiceImpl(dao);
        OrderController controller = new OrderController(view, service);
        controller.run();
    }
}
