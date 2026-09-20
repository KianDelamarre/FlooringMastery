package com.flooringmastery.dao;

import com.flooringmastery.dto.Order;
import com.flooringmastery.dto.Tax;
import com.flooringmastery.dto.Tax;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class TaxDaoFileImpl implements  TaxDao{

    private final String DELIMITER ;
    
    private final String TAX_FILE;

    Map<String, Tax> taxes = new HashMap<>();



    public TaxDaoFileImpl(String delimiter, String taxFile){
        this.DELIMITER=delimiter;
        this.TAX_FILE=taxFile;

        loadFile();
    }


    
    @Override
    public Map<String, Tax> getAllTaxes() {
        return taxes;
    }


    public Tax getTax(String StateAbbr){
        return this.taxes.get(StateAbbr);
    }





    private void loadFile() {
        Map<String, Tax> taxes = new HashMap<>();

        try {
            Scanner sc = new Scanner(
                    new BufferedReader(new FileReader(TAX_FILE)));

            while (sc.hasNextLine()) {
                String currentLine = sc.nextLine();

                // Skip empty lines
                if (currentLine.trim().isEmpty()) {
                    continue;
                }

                Tax currentTax = unmarshalLine(currentLine);
                taxes.put(currentTax.getStateAbr(), currentTax);
            }

        }
        catch(Exception ex){
            throw new RuntimeException("Could not read Order data from file: " + TAX_FILE, ex);
        }

        this.taxes=taxes;


    }


    public Tax unmarshalLine(String line){
        //tax entry format
        //StateAbbr::State::TaxRate
        String[] parts = line.split(DELIMITER);

        Tax tax = new Tax();

        tax.setStateAbr(parts[0]);
        tax.setState(parts[1]);
        tax.setTaxRate(parseBigDecimal(parts[2]));

        return tax;
    }



    public BigDecimal parseBigDecimal(String num) {
        return new BigDecimal(num);
    }


}


