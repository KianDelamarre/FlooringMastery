package com.flooringmastery.dao;

import com.flooringmastery.dto.Tax;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TaxDaoTest {

    private TaxDao testDao;

    //Actual taxs file since this Dao is readonly
    private static final String ACTUAL_TAX_FILE = "Data" + File.separator + "tax.txt";
    private static final String DELIMITER = "::";

    @BeforeEach
    public void setUp() {
        // Point the DAO directly at the real project file
        testDao = new TaxDaoFileImpl(DELIMITER, ACTUAL_TAX_FILE);
    }

    @Test
    public void testGetAllTaxs() {
        // ACT
        List<Tax> taxs = testDao.getAllTaxes();

        // ASSERT
        assertNotNull(taxs, "Tax list should not be null.");
        assertFalse(taxs.isEmpty(), "Real tax file should contain at least one tax.");
    }

    @Test
    public void testGetTaxSuccess() {
        // ACT - Query a known tax entry from your actual file
        Tax tax = testDao.getTax("TX");

        // ASSERT
        assertNotNull(tax, "TX should exist in the real tax file.");
        assertEquals("TX", tax.getStateAbr(), "State abbreviation should match that in file");
        assertNotNull(tax.getState(), "State should be populated.");
        assertNotNull(tax.getTaxRate(), "Tax rate should be populated.");
    }

    @Test
    public void testGetTaxWithWhitespace() {
        // ACT
        Tax tax = testDao.getTax("  TX  ");

        // ASSERT
        assertNotNull(tax, "Whitespace-padded query should locate 'TX' in real data.");
        assertEquals("TX", tax.getStateAbr());
    }

    @Test
    public void testGetTaxNotFound() {
        // ACT
        Tax tax = testDao.getTax("NonExistentTax12345");

        // ASSERT
        assertNull(tax, "Querying a non-existent tax against real data should return null.");
    }

    @Test
    public void testGetTaxNullInput() {
        // ACT
        Tax tax = testDao.getTax(null);

        // ASSERT
        assertNull(tax, "Null input should return null.");
    }

    @Test
    public void testUnmarshalLine() {
        // ARRANGE - Unit test for parsing logic using isolated text line
        TaxDaoFileImpl daoImpl = (TaxDaoFileImpl) testDao;
        String line = "TX::Texas::4.50";

        // ACT
        Tax result = daoImpl.unmarshalLine(line);

        // ASSERT
        assertNotNull(result);
        assertEquals("TX", result.getStateAbr());
        assertEquals("Texas", result.getState());
        assertEquals(new BigDecimal("4.50"), result.getTaxRate());
    }
}
