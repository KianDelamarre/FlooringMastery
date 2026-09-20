package com.flooringmastery.dao;

import com.flooringmastery.dto.Product;

import java.util.List;

public interface ProductDao {
    public List<Product> getAllProducts();

    public Product getProduct(String productType);
}
