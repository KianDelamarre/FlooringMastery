package com.flooringmastery.dao;

import com.flooringmastery.dto.Product;
import com.flooringmastery.dto.Product;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ProductDaoFileImpl implements  ProductDao{

    private Map<String, Product> allProducts = new HashMap<>();
    
    private final String DELIMITER;

    private final String PRODUCT_FILE;


    
    public ProductDaoFileImpl(String delimiter, String productFile){
        this.DELIMITER=delimiter;
        this.PRODUCT_FILE=productFile;

        loadFile();
    }

    @Override
    public List<Product> getAllProducts() {
        return List.of();
    }


    public Product getProduct(String productType){
        if(productType == null){
            return null;
        }

        String normalisedKey = productType.trim().toUpperCase(); //transform input to upper case so search can be case insensitive
        return this.allProducts.get(normalisedKey);
    }





    private void loadFile() {
        Map<String, Product> products = new HashMap<>();

        try {
            Scanner sc = new Scanner(
                    new BufferedReader(new FileReader(PRODUCT_FILE)));

            while (sc.hasNextLine()) {
                String currentLine = sc.nextLine();

                // Skip empty lines
                if (currentLine.trim().isEmpty()) {
                    continue;
                }

                Product currentProduct = unmarshalLine(currentLine);
                products.put(currentProduct.getProductType(), currentProduct);
            }

        }
        catch(Exception ex){
            throw new RuntimeException("Could not read Order data from file: " + PRODUCT_FILE, ex);
        }

        this.allProducts=products;


    }


    public Product unmarshalLine(String line){
        //product entry format
        //Tile::3.50::4.15
        String[] parts = line.split(DELIMITER);

        Product product = new Product();

        product.setProductType(parts[0]);
        product.setCostPerSquareFoot(parseBigDecimal(parts[1]));
        product.setLabourCostPerSquareFoot(parseBigDecimal(parts[2]));

        return product;
    }



    public BigDecimal parseBigDecimal(String num) {
        return new BigDecimal(num);
    }
}
