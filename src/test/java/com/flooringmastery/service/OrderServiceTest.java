package com.flooringmastery.service;

import com.flooringmastery.dto.Order;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.core.Local;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OrderServiceTest {

    private OrderService service;

    public OrderServiceTest(){
        ApplicationContext ctx =
                new ClassPathXmlApplicationContext("applicationContext.xml");
        service =
                ctx.getBean("serviceLayer", OrderService.class);
    }


    @Test
    public void testGetOrdersForDateSuccess() throws Exception{
        //ARRANGE
        LocalDate testDate = LocalDate.parse("2025-08-21");

        // ACT
        List<Order> orders = service.getOrdersForDate(testDate);

        // ASSERT
        assertNotNull(orders, "The list of orders should not be null.");
        assertEquals(1, orders.size(), "Should return exactly 1 order from stub.");

        Order onlyOrder = orders.get(0);
        assertEquals(0, onlyOrder.getOrderNumber(), "Order number should match stub data.");
        assertEquals("Ada Lovelace", onlyOrder.getCustomerName(), "Customer name should match stub data.");
    }

    @Test
    public void testCalculateFinalOrder() throws Exception{
        //ARRANGE
        Order testOrder = new Order();
        testOrder.setOrderDate(LocalDate.parse("2026-10-25"));
        testOrder.setCustomerName("Eva Smith");
        testOrder.setStateAbbr("TX");
        testOrder.setProductType("Tile");
        testOrder.setArea(new BigDecimal("249"));

        //ACT
        Order order = service.calculateFinalOrder(testOrder);


        //ASSERT
        assertNotNull(order, "The returned order should not be null");
        assertEquals("Tile", order.getProductType(), "Orders product type should match test order");
        assertEquals("Eva Smith", order.getCustomerName(), "Orders customer name should match test order");
        assertEquals(new BigDecimal("2381.06"), order.getTotal());

    }


}
