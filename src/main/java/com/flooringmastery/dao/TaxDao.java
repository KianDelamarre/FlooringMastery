package com.flooringmastery.dao;

import com.flooringmastery.dto.Tax;

import java.util.List;
import java.util.Map;

public interface TaxDao {
    public Map<String, Tax> getAllTaxes();
}
