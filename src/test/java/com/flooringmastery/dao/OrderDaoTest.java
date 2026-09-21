package com.flooringmastery.dao;

import com.flooringmastery.dto.Order;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderDaoTest {

    private OrderDao testDao;

    private static final String TEST_ORDER_FOLDER = "test_orders";
    private static final String TEST_DATA_FOLDER = "test_data";
    private static final String DELIMITER = "::";
    private static final String BACKUP_FILE = new File("Backup" + File.separator + "DataExport.txt").toString();

    public OrderDaoTest(){

    }

    @BeforeEach
    public void setUp() {
        // Clean out all files before each test runs
        cleanTestFiles();

        // Initialize fresh DAO with clean folders
        testDao = new OrderDaoFileImpl(DELIMITER, TEST_ORDER_FOLDER, TEST_DATA_FOLDER, BACKUP_FILE);
    }

    @AfterEach
    public void tearDown() {
        // Clean out all files after each test finishes
        cleanTestFiles();
    }

    private void cleanTestFiles() {
        // Clear contents of test order and test data folders
        cleanDirectoryContents(new File(TEST_ORDER_FOLDER));
        cleanDirectoryContents(new File(TEST_DATA_FOLDER));

        // Clear backup file and its parent folder
        File exportFile = new File(BACKUP_FILE);
        if (exportFile.exists()) {
            exportFile.delete();
        }

        File backupDir = exportFile.getParentFile();
        if (backupDir != null && backupDir.exists()) {
            cleanDirectoryContents(backupDir);
            backupDir.delete();
        }
    }

    private void cleanDirectoryContents(File dir) {
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.isDirectory()) {
                        cleanDirectoryContents(f);
                    }
                    // Delete both files and now empty subdirectories
                    if (!f.delete()) {
                        f.deleteOnExit();
                    }
                }
            }
        }
    }


    private Order createSampleOrder(LocalDate date, String customerName) {
        Order order = new Order();
        order.setOrderDate(date);
        order.setCustomerName(customerName);
        order.setStateAbbr("TX");
        order.setTaxRate(new BigDecimal("4.45"));
        order.setProductType("Tile");
        order.setArea(new BigDecimal("250.00"));
        order.setCostPerSquareFoot(new BigDecimal("3.50"));
        order.setLabourCostPerSquareFoot(new BigDecimal("4.15"));
        order.setMaterialCost(new BigDecimal("875.00"));
        order.setLabourCost(new BigDecimal("1037.50"));
        order.setTax(new BigDecimal("85.11"));
        order.setTotal(new BigDecimal("1997.61"));
        return order;
    }



    @Test
    public void testAddAndGetOrder() throws Exception {
        // ARRANGE
        LocalDate testDate = LocalDate.of(2026, 10, 25);
        Order sample = createSampleOrder(testDate, "Ada Lovelace");

        // ACT
        Order addedOrder = testDao.addOrder(sample);

        // ASSERT - Check returned object and order number assignment
        assertNotNull(addedOrder, "Added order should not be null.");
        assertEquals(0, addedOrder.getOrderNumber(), "First added order number should be 0.");

        // ACT - Retrieve from DAO
        Order retrievedOrder = testDao.getOrder(testDate, 0);

        // ASSERT - Check in-memory match
        assertNotNull(retrievedOrder, "Retrieved order should not be null.");
        assertEquals("Ada Lovelace", retrievedOrder.getCustomerName());
        assertEquals("TX", retrievedOrder.getStateAbbr());
        assertEquals(new BigDecimal("1997.61"), retrievedOrder.getTotal());
    }

    @Test
    public void testPersistenceAcrossInstances() throws Exception {
        // ARRANGE: Add order using first DAO instance
        LocalDate testDate = LocalDate.of(2026, 10, 25);
        Order sample = createSampleOrder(testDate, "Grace Hopper");
        testDao.addOrder(sample);

        // ACT: Instantiate a second DAO pointing to the same folder (simulates app restart)
        OrderDao reloadedDao = new OrderDaoFileImpl(DELIMITER, TEST_ORDER_FOLDER, TEST_DATA_FOLDER, BACKUP_FILE);

        // ASSERT: Verify order was read back from the file correctly
        Order retrievedOrder = reloadedDao.getOrder(testDate, 0);
        assertNotNull(retrievedOrder, "Order should be loaded from disk on DAO initialization.");
        assertEquals("Grace Hopper", retrievedOrder.getCustomerName());

        // Verify next order number auto-increment survived restart
        Order secondSample = createSampleOrder(testDate, "Alan Turing");
        Order secondAdded = reloadedDao.addOrder(secondSample);
        assertEquals(1, secondAdded.getOrderNumber(), "Next order number should resume from max_order_number file value + 1.");
    }

    @Test
    public void testGetOrdersForDate() throws Exception {
        // ARRANGE
        LocalDate testDate = LocalDate.of(2026, 10, 25);
        testDao.addOrder(createSampleOrder(testDate, "Customer One"));
        testDao.addOrder(createSampleOrder(testDate, "Customer Two"));

        // ACT
        List<Order> ordersForDate = testDao.getOrdersForDate(testDate);

        // ASSERT
        assertNotNull(ordersForDate, "List should not be null.");
        assertEquals(2, ordersForDate.size(), "Should return exactly 2 orders.");
    }

    @Test
    public void testGetOrdersForDateNotFound() {
        // ARRANGE
        LocalDate nonExistentDate = LocalDate.of(1999, 1, 1);

        // ACT
        List<Order> orders = testDao.getOrdersForDate(nonExistentDate);

        // ASSERT
        assertNull(orders, "Querying date with no orders should return null.");
    }

    @Test
    public void testEditOrder() throws Exception {
        // ARRANGE
        LocalDate testDate = LocalDate.of(2026, 10, 25);
        Order original = testDao.addOrder(createSampleOrder(testDate, "Original Name"));

        // ACT - Modify fields
        original.setCustomerName("Updated Name");
        original.setArea(new BigDecimal("500.00"));
        Order edited = testDao.editOrder(original);

        // ASSERT
        assertNotNull(edited);
        assertEquals("Updated Name", edited.getCustomerName());

        // Re-read from DAO to verify change persisted in memory map
        Order retrieved = testDao.getOrder(testDate, 0);
        assertEquals("Updated Name", retrieved.getCustomerName());

        // Verify disk file updated by re-instantiating DAO
        OrderDao freshDao = new OrderDaoFileImpl(DELIMITER, TEST_ORDER_FOLDER, TEST_DATA_FOLDER, BACKUP_FILE);
        Order reloaded = freshDao.getOrder(testDate, 0);
        assertEquals("Updated Name", reloaded.getCustomerName());
    }

    @Test
    public void testRemoveOrder() throws Exception {
        // ARRANGE
        LocalDate testDate = LocalDate.of(2026, 10, 25);
        Order order1 = testDao.addOrder(createSampleOrder(testDate, "Order To Keep"));
        Order order2 = testDao.addOrder(createSampleOrder(testDate, "Order To Remove"));

        // ACT
        Order removed = testDao.removeOrder(testDate, order2.getOrderNumber());

        // ASSERT
        assertNotNull(removed, "Removed order object should be returned.");
        assertEquals("Order To Remove", removed.getCustomerName());

        // Verify it is gone from memory
        assertNull(testDao.getOrder(testDate, order2.getOrderNumber()), "Removed order should not exist in DAO.");

        // Verify remaining order is still intact
        List<Order> remaining = testDao.getOrdersForDate(testDate);
        assertEquals(1, remaining.size());
        assertEquals("Order To Keep", remaining.get(0).getCustomerName());
    }

    @Test
    public void testExportAllDataToFile() throws Exception {
        // ARRANGE
        LocalDate date1 = LocalDate.of(2026, 10, 25);
        LocalDate date2 = LocalDate.of(2026, 11, 12);
        testDao.addOrder(createSampleOrder(date1, "Order 1"));
        testDao.addOrder(createSampleOrder(date2, "Order 2"));

        // ACT
        testDao.exportAllDataToFile();

        // ASSERT
        File backupFile = new File(BACKUP_FILE);
        assertTrue(backupFile.exists(), "Backup export file should be created.");
        assertTrue(backupFile.length() > 0, "Backup file should not be empty.");
    }


}
