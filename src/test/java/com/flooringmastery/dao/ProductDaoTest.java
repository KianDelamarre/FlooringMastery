package com.flooringmastery.dao;

import com.flooringmastery.dto.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductDaoTest {

    private ProductDao testDao;

    //Actual products file since this Dao is readonly
    private static final String ACTUAL_PRODUCT_FILE = "Data" + File.separator + "product.txt";
    private static final String DELIMITER = "::";

    @BeforeEach
    public void setUp() {
        // Point the DAO directly at the real project file
        testDao = new ProductDaoFileImpl(DELIMITER, ACTUAL_PRODUCT_FILE);
    }

    @Test
    public void testGetAllProducts() {
        // ACT
        List<Product> products = testDao.getAllProducts();

        // ASSERT
        assertNotNull(products, "Product list should not be null.");
        assertFalse(products.isEmpty(), "Real product file should contain at least one product.");
    }

    @Test
    public void testGetProductSuccess() {
        // ACT - Query a known product entry from your actual file
        Product product = testDao.getProduct("Tile");

        // ASSERT
        assertNotNull(product, "Tile should exist in the real product file.");
        assertEquals("Tile", product.getProductType());
        assertNotNull(product.getCostPerSquareFoot(), "Cost per sq ft should be populated.");
        assertNotNull(product.getLabourCostPerSquareFoot(), "Labor cost per sq ft should be populated.");
    }

    @Test
    public void testGetProductWithWhitespace() {
        // ACT
        Product product = testDao.getProduct("  Tile  ");

        // ASSERT
        assertNotNull(product, "Whitespace-padded query should locate 'Tile' in real data.");
        assertEquals("Tile", product.getProductType());
    }

    @Test
    public void testGetProductNotFound() {
        // ACT
        Product product = testDao.getProduct("NonExistentProduct12345");

        // ASSERT
        assertNull(product, "Querying a non-existent product against real data should return null.");
    }

    @Test
    public void testGetProductNullInput() {
        // ACT
        Product product = testDao.getProduct(null);

        // ASSERT
        assertNull(product, "Null input should return null.");
    }

    @Test
    public void testUnmarshalLine() {
        // ARRANGE - Unit test for parsing logic using isolated text line
        ProductDaoFileImpl daoImpl = (ProductDaoFileImpl) testDao;
        String line = "Wood::5.15::4.75";

        // ACT
        Product result = daoImpl.unmarshalLine(line);

        // ASSERT
        assertNotNull(result);
        assertEquals("Wood", result.getProductType());
        assertEquals(new BigDecimal("5.15"), result.getCostPerSquareFoot());
        assertEquals(new BigDecimal("4.75"), result.getLabourCostPerSquareFoot());
    }
}
