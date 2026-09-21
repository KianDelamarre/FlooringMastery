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

    /////////  getOrdersForDate //////////

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

    /////////  getOrdersForDate //////////

    ////// calculate Final order ///////

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
    public void testCalculateFinalOrderCaseInsensitiveState() throws Exception {
        // ARRANGE
        Order testOrder = new Order();
        testOrder.setOrderDate(LocalDate.parse("2026-10-25"));
        testOrder.setCustomerName("Eva Smith");
        testOrder.setStateAbbr("tx"); // lowercase input
        testOrder.setProductType("Tile");
        testOrder.setArea(new BigDecimal("249"));

        // ACT
        Order order = service.calculateFinalOrder(testOrder);

        // ASSERT
        assertNotNull(order, "Order should not be null.");
        assertEquals("TX", order.getStateAbbr(), "State should be normalized to uppercase 'TX'.");
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
    public void testCalculateFinalOrderSmallArea() {
        // ARRANGE
        Order testOrder = new Order();
        testOrder.setOrderDate(LocalDate.parse("2026-10-25"));
        testOrder.setCustomerName("Eva Smith");
        testOrder.setStateAbbr("TX");
        testOrder.setProductType("Tile");
        testOrder.setArea(new BigDecimal("99.99"));  //area too small

        // ACT & ASSERT
        assertThrows(
                InvalidOrderException.class,
                () -> service.calculateFinalOrder(testOrder),
                "Expected calculateFinalOrder to throw InvalidOrderException for an area below 100, but it didn't."
        );
    }

    @Test
    public void testCalculateFinalOrderNullArea() {
        // ARRANGE
        Order testOrder = new Order();
        testOrder.setOrderDate(LocalDate.parse("2026-10-25"));
        testOrder.setCustomerName("Eva Smith");
        testOrder.setStateAbbr("TX");
        testOrder.setProductType("Tile");
        testOrder.setArea(null);  //area too small

        // ACT & ASSERT
        assertThrows(
                InvalidOrderException.class,
                () -> service.calculateFinalOrder(testOrder),
                "Expected calculateFinalOrder to throw InvalidOrderException for a null area, but it didn't."
        );
    }


    ////// calculate Final order ///////


    /////////  addOrder //////////

    @Test
    public void testAddOrderSuccess() throws Exception {

        // ARRANGE
        Order testOrder = new Order();
        testOrder.setOrderDate(LocalDate.parse("2026-10-25"));
        testOrder.setCustomerName("Eva Smith");
        testOrder.setStateAbbr("TX");
        testOrder.setProductType("Tile");
        testOrder.setArea(new BigDecimal("249"));


        // ACT
        Order addedOrder = service.addOrder(testOrder);

        // ASSERT
        assertNotNull(addedOrder, "The returned order should not be null");
        assertEquals("Tile", addedOrder.getProductType(), "Orders product type should match test order");
        assertEquals("Eva Smith", addedOrder.getCustomerName(), "Orders customer name should match test order");
        assertEquals(new BigDecimal("3.50"), addedOrder.getCostPerSquareFoot());
        assertEquals(new BigDecimal("871.50"), addedOrder.getMaterialCost());
        assertEquals(new BigDecimal("1033.35"), addedOrder.getLabourCost());
        assertEquals(new BigDecimal("476.21"), addedOrder.getTax());
        assertEquals(new BigDecimal("2381.06"), addedOrder.getTotal());
    }


    /////////  addOrder //////////

    /////////  editOrder //////////

    @Test
    public void testEditOrderSuccess() throws Exception {
        // ARRANGE
        Order editRequest = new Order();
        editRequest.setOrderDate(LocalDate.parse("2026-10-25"));
        editRequest.setOrderNumber(0);
        editRequest.setCustomerName("Ada Lovelace Updated");
        editRequest.setStateAbbr("TX");
        editRequest.setProductType("Tile");
        editRequest.setArea(new BigDecimal("250"));

        // ACT
        Order editedOrder = service.editOrder(editRequest);

        // ASSERT
        assertNotNull(editedOrder, "The edited order should not be null.");
        assertEquals(0, editedOrder.getOrderNumber(), "Order number should match stub.");
        assertNotNull(editedOrder.getTotal(), "Calculated fields like total should be populated.");
    }

    @Test
    public void testEditOrderFailsInvalidData() {
        // ARRANGE
        Order editRequest = new Order();
        editRequest.setOrderDate(LocalDate.parse("2026-10-25"));
        editRequest.setOrderNumber(0);
        editRequest.setCustomerName("Ada Lovelace");
        editRequest.setStateAbbr("INVALID_STATE"); // bad state
        editRequest.setProductType("Tile");
        editRequest.setArea(new BigDecimal("250"));

        // ACT & ASSERT
        assertThrows(
                InvalidOrderException.class,
                () -> service.editOrder(editRequest),
                "Expected editOrder to throw InvalidOrderException for an invalid state."
        );
    }

    /////////  editOrder //////////

    /////////  getOrder //////////

    @Test
    public void getOrderSuccess() throws Exception {

        // ARRANGE
        LocalDate date = LocalDate.parse("2025-08-21");
        int number = 0;


        // ACT
        Order retrievedOrder = service.getOrder(date, number);

        // ASSERT
        assertNotNull(retrievedOrder, "The returned order should not be null");
        assertEquals("Tile", retrievedOrder.getProductType(), "Orders product type should match test order");
        assertEquals("Ada Lovelace", retrievedOrder.getCustomerName(), "Orders customer name should match test order");
        assertEquals(new BigDecimal("3.50"), retrievedOrder.getCostPerSquareFoot());
        assertEquals(new BigDecimal("871.50"), retrievedOrder.getMaterialCost());
        assertEquals(new BigDecimal("1033.35"), retrievedOrder.getLabourCost());
        assertEquals(new BigDecimal("476.21"), retrievedOrder.getTax());
        assertEquals(new BigDecimal("2381.06"), retrievedOrder.getTotal());
    }

    /////////  getOrder //////////


    /////////  removeOrder //////////


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

    @Test
    public void testRemoveNullOrder() throws Exception {

        // ARRANGE: Create an order object with the exact keys present in the stub
        Order orderToRemove = null;


        // ASSERT
        assertThrows(
                InvalidOrderException.class,
                () -> service.removeOrder(orderToRemove),
                "Expected calculateFinalOrder to throw InvalidOrderException for a past date, but it didn't."
        );
    }

    /////////  removeOrder //////////





}
