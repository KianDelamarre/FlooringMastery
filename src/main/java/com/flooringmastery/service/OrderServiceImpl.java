package com.flooringmastery.service;

import com.flooringmastery.dao.*;
import com.flooringmastery.dto.Order;
import com.flooringmastery.dto.Product;
import com.flooringmastery.dto.Tax;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

public class OrderServiceImpl implements OrderService{

    private OrderDao orderDao;
    private TaxDao taxDao;
    private ProductDao productDao;

    private static final BigDecimal MIN_AREA = new BigDecimal("100");

    public OrderServiceImpl(OrderDao orderDao,TaxDao tax, ProductDao product){
        this.orderDao = orderDao;
        this.taxDao = tax;
        this.productDao = product;

    }

    @Override
    public int getNextOrderNumber() {
        return 0;
    }

    public Order calculateFinalOrder(Order order){
        //validate inputted values
        validateOrderDate(order.getOrderDate());
        validateCustomerName(order.getCustomerName());
        order.setState(validateState(order.getState())); //set the state to the capitalised version returned by the validation
        order.setProductType(validateProductType(order.getProductType()));  //set the product type to the capitalised version returned by the validation
        validateArea(order.getArea());

        //get required values from Tax and Products using prevalidated values
        Product productDto = productDao.getProduct(order.getProductType());
        BigDecimal costPerSquareFoot = productDto.getCostPerSquareFoot();
        BigDecimal labourCostPerSquareFoot = productDto.getLabourCostPerSquareFoot();

        Tax taxDto = taxDao.getTax(order.getState());
        BigDecimal taxRate = taxDto.getTaxRate();

        //set values in the order that come directly from the tax and products on file
        order.setCostPerSquareFoot(costPerSquareFoot);
        order.setLabourCostPerSquareFoot(labourCostPerSquareFoot);
        order.setTaxRate(taxRate);

        //calculate calculable costs
        BigDecimal materialCost = calculateMaterialCost(order.getArea(), costPerSquareFoot);
        BigDecimal labourCost = calculateLabourCost(order.getArea(), labourCostPerSquareFoot);
        BigDecimal tax = calculateTax(materialCost, labourCost, taxRate);
        BigDecimal total = calculateTotal(materialCost, labourCost, tax);

        //set calculated costs
        order.setMaterialCost(materialCost);
        order.setLabourCost(labourCost);
        order.setTax(tax);
        order.setTotal(total);

        return order;
    }

    @Override
    public Order addOrder(Order order) throws OrderPersistenceException {

        //persist
        orderDao.addOrder(order);

        return order;
    }

    @Override
    public Order getOrder(LocalDate date, int orderNumber) {
        return null;
    }

    @Override
    public Order editOrder(LocalDate orderDate, int orderNumber) {
        return null;
    }

    @Override
    public List<Order> getOrdersForDate(LocalDate date) throws OrderPersistenceException {
        return orderDao.getOrdersForDate(date);
    }

    @Override
    public Order removeOrder(LocalDate date, int OrderNumber) {
        return null;
    }

    @Override
    public void ExportData() {

    }

    @Override
    public List<Tax> getTaxes() {
        return List.of();
    }

    @Override
    public List<Product> getProducts() {
        return List.of();
    }



    //Order Date must be in the future
    private LocalDate validateOrderDate(LocalDate date){
        if(date == null || !date.isAfter(LocalDate.now())){
            throw new InvalidOrderException("Order date must be at least 1 day in the future");
        }

        return date;
    }

    //May not be blank and is limited to characters [a-z][0-9] as well as periods and comma characters. "Acme, Inc." is a valid name.
    private String validateCustomerName(String customerName){

        if(customerName == null || customerName.isEmpty() || !customerName.matches("(?i)[a-z0-9,.\\s]+")){
            throw new InvalidOrderException("Customer cannot be blank. may only contain alphanumerical characters as well as periods and command.  \"Acme, Inc.\" is a valid name.");
        }

        return customerName;
    }

    //Entered states must be checked against the tax file. If the state does not exist in the tax file, we cannot sell there.
    // If the tax file is modified to include the state, it should be allowed without changing the application code.
    private String validateState(String state){
        Tax tax = taxDao.getTax(state);
        if(tax == null || tax.getState() == null){  //since we got the state using that state, all we need to do is ensure the taxse stateAbbr isnt null, no need to again check it matches the inputted string
            throw new InvalidOrderException("State not present");
        }
        return tax.getState(); // returns the proper upper case version from the tax file
    }

    //Show a list of available products and pricing information to choose from.
    // Again, if a product is added to the file it should show up in the application without a code change.
    private String validateProductType (String productType){
        //exact same logic as get stateAbbr
        Product product = productDao.getProduct(productType);

        if(product == null || product.getProductType() == null){
            throw new InvalidOrderException("Product type not available");
        }

        return product.getProductType();
    }

    //the area must be a positive decimal. Minimum order size is 100 sq ft.
    private BigDecimal validateArea(BigDecimal area){

        if (area == null || area.compareTo(MIN_AREA) < 0) {
            throw new InvalidOrderException("Order area must be at least 100 sq ft.");
        }
        return area;
    }


    private BigDecimal calculateMaterialCost(BigDecimal area, BigDecimal costPerSqrFt){
        //MaterialCost = (Area * CostPerSquareFoot)
        BigDecimal materialCost = area.multiply(costPerSqrFt);
        return materialCost.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateLabourCost(BigDecimal area, BigDecimal labourCostPerSqrFt){
        //LaborCost = (Area * LaborCostPerSquareFoot)
        BigDecimal labourCost = area.multiply(labourCostPerSqrFt);
        return labourCost.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateTax(BigDecimal materialCost, BigDecimal labourCost, BigDecimal taxRate){
        //Tax = (MaterialCost + LaborCost) * (TaxRate/100)
        BigDecimal tax = (materialCost.add(labourCost)).multiply(taxRate.divide(new BigDecimal("100")));
        return tax.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateTotal(BigDecimal materialCost, BigDecimal labourCost, BigDecimal tax){
        //Total = (MaterialCost + LaborCost + Tax)
        BigDecimal total = materialCost.add(labourCost).add(tax);
        return total.setScale(2, RoundingMode.HALF_UP);
    }
}
