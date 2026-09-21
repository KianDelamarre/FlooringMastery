package com.flooringmastery.service;

import com.flooringmastery.dto.Order;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.core.Local;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        assertEquals(new BigDecimal("3.50"), order.getCostPerSquareFoot());
        assertEquals(new BigDecimal("871.50"), order.getMaterialCost());
        assertEquals(new BigDecimal("1033.35"), order.getLabourCost());
        assertEquals(new BigDecimal("476.21"), order.getTax());
        assertEquals(new BigDecimal("2381.06"), order.getTotal());
    }

    @Test
    public void testCalculateFinalOrderFailsBadDate() {
        // ARRANGE
        Order testOrder = new Order();
        testOrder.setOrderDate(LocalDate.parse("2024-10-25")); // Past date
        testOrder.setCustomerName("Eva Smith");
        testOrder.setStateAbbr("TX");
        testOrder.setProductType("Tile");
        testOrder.setArea(new BigDecimal("249"));

        // ACT & ASSERT
        assertThrows(
                InvalidOrderException.class,
                () -> service.calculateFinalOrder(testOrder),
                "Expected calculateFinalOrder to throw InvalidOrderException for a past date, but it didn't."
        );
    }


    @Test
    public void testCalculateFinalOrderFailsBadName() {
        // ARRANGE
        Order testOrder = new Order();
        testOrder.setOrderDate(LocalDate.parse("2026-10-25"));
        testOrder.setCustomerName("Eva Smith#");  //invalid name
        testOrder.setStateAbbr("TX");
        testOrder.setProductType("Tile");
        testOrder.setArea(new BigDecimal("249"));

        // ACT & ASSERT
        assertThrows(
                InvalidOrderException.class,
                () -> service.calculateFinalOrder(testOrder),
                "Expected calculateFinalOrder to throw InvalidOrderException for a name containing special characters, but it didn't."
        );
    }

    @Test
    public void testCalculateFinalOrderFailsBadState() {
        // ARRANGE
        Order testOrder = new Order();
        testOrder.setOrderDate(LocalDate.parse("2026-10-25"));
        testOrder.setCustomerName("Eva Smith");
        testOrder.setStateAbbr("OH");  //invalid state
        testOrder.setProductType("Tile");
        testOrder.setArea(new BigDecimal("249"));

        // ACT & ASSERT
        assertThrows(
                InvalidOrderException.class,
                () -> service.calculateFinalOrder(testOrder),
                "Expected calculateFinalOrder to throw InvalidOrderException for a state that isnt available, but it didn't."
        );
    }


    @Test
    public void testCalculateFinalOrderFailsBadProductType() {
        // ARRANGE
        Order testOrder = new Order();
        testOrder.setOrderDate(LocalDate.parse("2026-10-25"));
        testOrder.setCustomerName("Eva Smith");
        testOrder.setStateAbbr("TX");
        testOrder.setProductType("Wood");  //invalid product type
        testOrder.setArea(new BigDecimal("249"));

        // ACT & ASSERT
        assertThrows(
                InvalidOrderException.class,
                () -> service.calculateFinalOrder(testOrder),
                "Expected calculateFinalOrder to throw InvalidOrderException for invalid product type, but it didn't."
        );
    }

    @Test
    public void testCalculateFinalOrderInvalidArea() {
        // ARRANGE
        Order testOrder = new Order();
        testOrder.setOrderDate(LocalDate.parse("2026-10-25"));
        testOrder.setCustomerName("Eva Smith");
        testOrder.setStateAbbr("TX");
        testOrder.setProductType("Tile");
        testOrder.setArea(new BigDecimal("99.99"));  //invalid area

        // ACT & ASSERT
        assertThrows(
                InvalidOrderException.class,
                () -> service.calculateFinalOrder(testOrder),
                "Expected calculateFinalOrder to throw InvalidOrderException for an area below 100, but it didn't."
        );
    }




    @Test
    public void testAddOrderSuccess() throws Exception {

        // ARRANGE
        Order testOrder = new Order();
        testOrder.setOrderDate(LocalDate.parse("2026-10-25"));
        testOrder.setCustomerName("Eva Smith");
        testOrder.setStateAbbr("TX");
        testOrder.setProductType("Tile");
        testOrder.setArea(new BigDecimal("249"));


        // Calculate fields first as controller would
        Order calculatedOrder = service.calculateFinalOrder(testOrder);

        // ACT
        Order addedOrder = service.addOrder(calculatedOrder);

        // ASSERT
        assertNotNull(addedOrder, "The added order should not be null.");
        assertEquals("Eva Smith", addedOrder.getCustomerName(), "Customer name should match that passed into the order");
        assertEquals(new BigDecimal("2381.06"), addedOrder.getTotal(), "Total cost should match calculated order.");
    }


    @Test
    public void testRemoveSuccess() throws Exception {

        // ARRANGE: Create an order object with the exact keys present in the stub
        Order orderToRemove = new Order();
        orderToRemove.setOrderDate(LocalDate.parse("2025-08-21"));
        orderToRemove.setOrderNumber(0);

        // ACT
        Order removedOrder = service.removeOrder(orderToRemove);

        // ASSERT
        assertNotNull(removedOrder, "The removed order should not be null.");
        assertEquals("Ada Lovelace", removedOrder.getCustomerName(), "Customer name should match the stub data.");
        assertEquals(new BigDecimal("2381.06"), removedOrder.getTotal(), "Total cost should match stub data.");
    }




}
