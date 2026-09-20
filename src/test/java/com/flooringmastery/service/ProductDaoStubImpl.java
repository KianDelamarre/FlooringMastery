package com.flooringmastery.service;

import com.flooringmastery.dao.ProductDao;
import com.flooringmastery.dto.Order;
import com.flooringmastery.dto.Product;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDaoStubImpl implements ProductDao {
    public Product onlyProduct;

    public ProductDaoStubImpl(){
        onlyProduct = new Product();
        onlyProduct.setProductType("Tile");
        onlyProduct.setCostPerSquareFoot(new BigDecimal("3.50"));
        onlyProduct.setLabourCostPerSquareFoot(new BigDecimal("4.15"));
    }


    @Override
    public List<Product> getAllProducts() {
        return new ArrayList<>(List.of(this.onlyProduct));
    }

    @Override
    public Product getProduct(String productType) {
        if(productType.equals(this.onlyProduct.getProductType())) {
            return this.onlyProduct;
        }

        return null;
    }
}
