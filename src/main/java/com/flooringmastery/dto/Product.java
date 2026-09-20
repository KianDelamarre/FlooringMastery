package com.flooringmastery.dto;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {
    private String productType;

    private BigDecimal costPerSquareFoot;

    private BigDecimal labourCostPerSquareFoot;

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public BigDecimal getCostPerSquareFoot() {
        return costPerSquareFoot;
    }

    public void setCostPerSquareFoot(BigDecimal costPerSquareFoot) {
        this.costPerSquareFoot = costPerSquareFoot;
    }

    public BigDecimal getLabourCostPerSquareFoot() {
        return labourCostPerSquareFoot;
    }

    public void setLabourCostPerSquareFoot(BigDecimal labourCostPerSquareFoot) {
        this.labourCostPerSquareFoot = labourCostPerSquareFoot;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(productType, product.productType) && Objects.equals(costPerSquareFoot, product.costPerSquareFoot) && Objects.equals(labourCostPerSquareFoot, product.labourCostPerSquareFoot);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productType, costPerSquareFoot, labourCostPerSquareFoot);
    }

    @Override
    public String toString() {
        return "Product{" +
                "productType='" + productType + '\'' +
                ", costPerSquareFoot=" + costPerSquareFoot +
                ", labourCostPerSquareFoot=" + labourCostPerSquareFoot +
                '}';
    }
}
