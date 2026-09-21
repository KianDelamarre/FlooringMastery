package com.flooringmastery;

import com.flooringmastery.controller.OrderController;
import com.flooringmastery.dao.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    static void main(String[] args) throws Exception{
//        UserIo io = new UserIoConsoleImpl();
//        OrderView view = new OrderView(io);
//
//        OrderDao OrderDao = new OrderDaoFileImpl("::", "Orders", "Data");
//        TaxDao taxDao = new TaxDaoFileImpl("::", "Data/tax.txt");
//        ProductDao productDao = new ProductDaoFileImpl("::", "Data/product.txt");
//
//        OrderService service = new OrderServiceImpl(OrderDao, taxDao, productDao);
//        OrderController controller = new OrderController(view, service);
//        controller.run();


        ApplicationContext ctx =                      //import application context
                new ClassPathXmlApplicationContext("applicationContext.xml");

        OrderController controller =                  //instantiate a controller using the controller bean defined in the application context
                ctx.getBean("controller", OrderController.class);   //By passing in the bean id (controller), and the class name, as well as type (.class)
        controller.run();
    }
}
