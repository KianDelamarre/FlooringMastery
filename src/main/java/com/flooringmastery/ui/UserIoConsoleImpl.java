package com.flooringmastery.ui;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class UserIoConsoleImpl implements UserIo{
    Scanner scanner = new Scanner(System.in);

    @Override
    public void print(String message){
        System.out.println(message);
    }

    @Override
    public String readString(String prompt){
        print(prompt);
        return this.scanner.nextLine();
    }

    @Override
    public String readString(String prompt, List<String> options) {
        while (true) {
            print(prompt);
            String inputString = this.scanner.nextLine().trim();

            for (String option : options) {
                if (option.equalsIgnoreCase(inputString)) {  //makes input matching case insensitive
                    return option;
                }
            }

            print("Invalid input. Please choose from: " + options);
        }
    }

    @Override
    public int readInt(String prompt){
        while(true){
            try{
                String rawInput = readString(prompt);
                return Integer.parseInt(rawInput);
            }
            catch(NumberFormatException ex){
                print("Invalid input, enter an integer");
            }
        }
    }

    @Override
    public int readInt(String prompt, int min, int max) {
        while (true) {
            int input = readInt(prompt); // Delegated parsing & base error handling
            if (input >= min && input <= max) {
                return input;
            }
            print("Input out of range. Enter a value between " + min + " and " + max);
        }
    }



    @Override
    public double readDouble(String prompt){
        while(true){
            try{
                String rawInput = readString(prompt);
                return Double.parseDouble(rawInput);

            }
            catch(NumberFormatException ex){
                print("Invalid input, enter a double");
            }
        }
    }

    @Override
    public double readDouble(String prompt, double min, double max) {
        while (true) {
            double input = readDouble(prompt); // Delegated parsing & base error handling
            if (input >= min && input <= max) {
                return input;
            }
            print("Input out of range. Enter a value between " + min + " and " + max);
        }
    }

    @Override
    public float readFloat(String prompt){
        while(true){
            try{
                String rawInput = readString(prompt);
                return Float.parseFloat(rawInput);

            }
            catch(NumberFormatException ex){
                print("Invalid input, enter a float");
            }

        }
    }

    @Override
    public float readFloat(String prompt, float min, float max) {
        while (true) {
            float input = readFloat(prompt); // Delegated parsing & base error handling
            if (input >= min && input <= max) {
                return input;
            }
            print("Input out of range. Enter a value between " + min + " and " + max);
        }
    }

    @Override
    public long readLong(String prompt){
        while(true){
            try{
                String rawInput = readString(prompt);
                return Long.parseLong(rawInput);

            }
            catch(NumberFormatException ex){
                print("Invalid input, enter a long");
            }

        }
    }

    @Override
    public long readLong(String prompt, long min, long max) {
        while (true) {
            long input = readLong(prompt); // Delegated parsing & base error handling
            if (input >= min && input <= max) {
                return input;
            }
            print("Input out of range. Enter a value between " + min + " and " + max);
        }
    }

    @Override
    public BigDecimal readBigDecimal(String prompt) {
        while(true){
            try{
                String rawInput = readString(prompt);
                return new BigDecimal(rawInput);

            }
            catch(NumberFormatException ex){
                print("Invalid input, enter a number");
            }

        }
    }

    @Override
    public LocalDate readLocalDate(String prompt) {
        while(true){
            try{
                String rawInput = readString(prompt);
                return LocalDate.parse(rawInput, DateTimeFormatter.ofPattern("MMddyyyy"));

            }
            catch(DateTimeParseException ex){
                print("Invalid input, enter a date");
            }

        }
    }
}
