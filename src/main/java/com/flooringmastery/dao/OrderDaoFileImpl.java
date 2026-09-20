package com.flooringmastery.dao;

import com.flooringmastery.dto.Order;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class OrderDaoFileImpl implements  OrderDao{


    private final String DELIMITER;

    private final String ORDER_FOLDER;

    private final String DATA_FOLDER;

    private final String MAX_ORDER_NUMBER_FILE;

    private int nextAvailableOrderNumber;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMddyyyy");

    private Map<LocalDate,Map<Integer, Order>> orders = new HashMap<LocalDate, Map<Integer, Order>>();

    public OrderDaoFileImpl(String delimiter, String orderFolder, String dataFolder){
        this.DELIMITER=delimiter;
        this.ORDER_FOLDER=orderFolder;
        this.DATA_FOLDER=dataFolder;

        this.MAX_ORDER_NUMBER_FILE = DATA_FOLDER + File.separator + "max_order_number.txt";

        createDirectory(ORDER_FOLDER);
        createDirectory(DATA_FOLDER);

        loadAllOrdersFromAllFiles();

        loadNextOrderNumberAndSetNextAvailableOrderNumber();
    }


    @Override
    public Order addOrder(Order order) throws OrderPersistenceException{

        String filename = generateFileName(order.getOrderDate()); // generate filename

        String fullPath = ORDER_FOLDER + File.separator + filename; //generata fill file path
        order.setOrderNumber(nextAvailableOrderNumber);    //set order number using global var

        appendOrderToFile(fullPath, order);  //append this order to  the file

        //adding to local map //

        LocalDate date = order.getOrderDate();

        Map<Integer, Order> dateOrders = this.orders.get(date); //get the actual nested map containing this order by reference

        if (dateOrders == null) { //if null then create the map to be added
            dateOrders = new HashMap<>();
            orders.put(date, dateOrders);
        }

        //if not null, a date into the retrieved nested map
        dateOrders.put(order.getOrderNumber(), order);


        incrementOrderNumber(); //increment next available order number
        writeMaxOrderNumberToFile(); //write to file
        

        return order;
    }

    @Override
    public Order getOrder(LocalDate date, int orderNumber){
        Map<Integer, Order> dateOrders = this.orders.get(date); //get the actual nested map containing this order by reference

        if (dateOrders == null) {
            return null;
        }

        return dateOrders.get(orderNumber);
    }

    @Override
    public Order editOrder(LocalDate date, int orderNumber, Order order){

        Map<Integer, Order> dateOrders = this.orders.get(date); //get the actual nested map containing this order by reference

        if (dateOrders == null) { //if null then create the map to be added
            return null;
        }

        Order orderToEdit = dateOrders.get(orderNumber); //get specific order

        if(orderToEdit == null){
            return null;
        }

        dateOrders.put(orderNumber, order);  //overwrite existing order with new, edited order

        String filename = generateFileName(order.getOrderDate()); // generate filename
        String fullPath = ORDER_FOLDER + File.separator + filename; //generata fill file path

        writeOrdersToFile(fullPath, dateOrders.values().stream().toList()); //overwrite the existing file with the new list of orders for that date


        return order;

    }

    @Override
    public List<Order> getOrdersForDate(LocalDate date) {
        Map<Integer, Order> dateOrders = this.orders.get(date); //get the actual nested map containing this order by reference

        if (dateOrders == null) {
            return null;
        }

        return dateOrders.values().stream().toList();

    }

    @Override
    public Map<LocalDate, Map<Integer, Order>> getAllOrders(){
        return this.orders;
    }

    @Override
    public Order removeOrder(LocalDate date, int orderNumber){
        Map<Integer, Order> dateOrders = this.orders.get(date); //get the actual nested map containing this order by reference

        if (dateOrders == null) { //if null then create the map to be added
            return null;
        }

        Order order = dateOrders.get(orderNumber); //get specific order

        if(order == null){
            return null;
        }

        dateOrders.remove(orderNumber);  //remove order entry for that date in the nested local hashmap


        String filename = generateFileName(order.getOrderDate()); // generate filename
        String fullPath = ORDER_FOLDER + File.separator + filename; //generata fill file path

        writeOrdersToFile(fullPath, dateOrders.values().stream().toList()); //overwrite the existing file with the new list of orders for that date


        return order;
    }


    private int getLargestOrderNumber(){
        return 0;
    }


    private int updateLargestOrderNumberFile(){
        return 0;
    }

    private int getLargestOrder(){
        return 0;
    }

    //append a single Order to a file
    private Order appendOrderToFile(String filePath, Order order) throws OrderPersistenceException {
        try (PrintWriter out = new PrintWriter(new FileWriter(filePath, true))) {

            out.println(marshalRecord(order));
            out.flush();
            out.close();
            return order;
        }
        catch (Exception e) {
            throw new OrderPersistenceException("Could not write order #" + order.getOrderNumber() + " to file.", e);
        }
    }

    //overwrite all orders on a file with an updated list of orders
    private void writeOrdersToFile(String filePath, List<Order> orders) throws OrderPersistenceException {
        try (PrintWriter out = new PrintWriter(new FileWriter(filePath))) {

            for (Order order : orders) {
                out.println(marshalRecord(order));
            }

            out.flush();
        }
        catch (Exception e) {
            throw new OrderPersistenceException("Could not save batch of orders to " + filePath, e);
        }
    }

    private void loadNextOrderNumberAndSetNextAvailableOrderNumber(){
        File file = new File(MAX_ORDER_NUMBER_FILE);

        if (!file.exists()) {
            nextAvailableOrderNumber = 0;
            writeMaxOrderNumberToFile();
            return;
        }

        try{
            Scanner sc = new Scanner(
                    new BufferedReader(new FileReader(MAX_ORDER_NUMBER_FILE)));

            if(sc.hasNext()){
                String currentLine = sc.nextLine();
                int maxOrderNumber = parseInt(currentLine);
                maxOrderNumber++;

                nextAvailableOrderNumber = maxOrderNumber;
            }

            else {
                nextAvailableOrderNumber = 0;
            }
        }

        catch(Exception ex){
            throw new RuntimeException("Could not read Order number from file: " + MAX_ORDER_NUMBER_FILE, ex);
        }

    }

    private void writeMaxOrderNumberToFile(){
        try (PrintWriter out = new PrintWriter(new FileWriter(MAX_ORDER_NUMBER_FILE))) {

            out.println(nextAvailableOrderNumber-1);
            out.flush();
            out.close();
        }
        catch (Exception e) {
            throw new OrderPersistenceException("Could not write max order number #" + (nextAvailableOrderNumber-1) + " to file.", e);
        }
    }

    private void incrementOrderNumber(){
        nextAvailableOrderNumber++;
    }


    private void loadAllOrdersFromAllFiles(){

        File directory = new File(ORDER_FOLDER);
        String[] files = directory.list();

        if(files == null){
            return;
        }

        for(String file : files){

            LocalDate date = extractDateFromFile(file);

            String fullPath = ORDER_FOLDER + File.separator+ file;

            this.orders.put(date, loadOrdersFromFile(fullPath));
        }
    }

    private Map<Integer,Order> loadOrdersFromFile(String filePath){

        Map<Integer, Order> orders = new HashMap<Integer,Order>();

        try{
            Scanner sc = new Scanner(
                    new BufferedReader(new FileReader(filePath)));

            while (sc.hasNextLine()) {
                String currentLine = sc.nextLine();

                // Skip empty lines
                if (currentLine.trim().isEmpty()) {
                    continue;
                }

                Order currentOrder = unmarshalLine(currentLine);
                orders.put(currentOrder.getOrderNumber(), currentOrder);
            }

        }

        catch(Exception ex){
            throw new RuntimeException("Could not read Order data from file: " + filePath, ex);
        }

        return orders;
    }

    private LocalDate extractDateFromFile(String fileName){
        if (fileName.startsWith("Orders_") && fileName.endsWith(".txt")) {

            String date = fileName.substring(7, fileName.lastIndexOf('.'));
            return parseDate(date);
        }
        else{
            throw new RuntimeException("Invalid file name");
        }
    }

    private String generateFileName(LocalDate date){
        return "Orders_" + dateToString(date) + ".txt";
    }


    private String dateToString(LocalDate date){
        if (date == null) {
            return ""; // or return null, depending on your DAO preference
        }
        return date.format(DATE_FORMATTER);
    }

    private LocalDate parseDate(String date){
        return LocalDate.parse(date, DATE_FORMATTER);
    }


    private void createDirectory(String dirName) {

        File directory = new File(dirName);

        boolean created = directory.mkdirs();

        if (created) {
            System.out.println("Directory was created.");
        } else if (directory.exists()) {
            System.out.println("Directory already exists.");
        } else {
            System.out.println("Failed to create directory.");
        }

    }


    public Order unmarshalLine(String line){
        //order entry format
        //OrderNumber::CustomerName::State::TaxRate::ProductType::Area::CostPerSquareFoot::LabourCostPerSquareFoot::MaterialCost::LabourCost:Tax:Total
        String[] parts = line.split(DELIMITER);

        Order order = new Order();

        order.setOrderNumber(parseInt(parts[0]));
        order.setCustomerName(parts[1]);
        order.setStateAbbr(parts[2]);
        order.setTaxRate(parseBigDecimal(parts[3]));
        order.setProductType(parts[4]);
        order.setArea(parseBigDecimal(parts[5]));
        order.setCostPerSquareFoot(parseBigDecimal(parts[6]));
        order.setLabourCostPerSquareFoot(parseBigDecimal(parts[7]));
        order.setMaterialCost(parseBigDecimal(parts[8]));
        order.setLabourCost(parseBigDecimal(parts[9]));
        order.setTax(parseBigDecimal(parts[10]));
        order.setTotal(parseBigDecimal(parts[11]));

        return order;
    }

    public String marshalRecord(Order order) {
        if (order == null) {
            return "";
        }

        String sb = order.getOrderNumber() + DELIMITER +
                order.getCustomerName() + DELIMITER +
                order.getStateAbbr() + DELIMITER +
                order.getTaxRate() + DELIMITER +
                order.getProductType() + DELIMITER +
                order.getArea() + DELIMITER +
                order.getCostPerSquareFoot() + DELIMITER +
                order.getLabourCostPerSquareFoot() + DELIMITER +
                order.getMaterialCost() + DELIMITER +
                order.getLabourCost() + DELIMITER +
                order.getTax() + DELIMITER +
                order.getTotal();

        return sb;
    }

    private int parseInt(String num){
        return Integer.parseInt(num);
    }

    public BigDecimal parseBigDecimal(String num) {
                return new BigDecimal(num);
    }





}
