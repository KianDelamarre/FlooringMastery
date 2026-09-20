package com.flooringmastery.service;

import com.flooringmastery.dao.TaxDao;
import com.flooringmastery.dto.Tax;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaxDaoStubImpl implements TaxDao {

    public Tax onlyTax;

    public TaxDaoStubImpl(){
        onlyTax=new Tax();
        onlyTax.setStateAbr("TX");
        onlyTax.setState("Texas");
        onlyTax.setTaxRate(new BigDecimal("25"));
    }

    @Override
    public List<Tax> getAllTaxes() {
        return new ArrayList<>(List.of(this.onlyTax));
    }

    @Override
    public Tax getTax(String stateAbbr) {
        if (stateAbbr != null && stateAbbr.equals(onlyTax.getStateAbr())) {
            return onlyTax;

        }

        return null;
    }
}
