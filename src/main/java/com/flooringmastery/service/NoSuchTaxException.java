package com.flooringmastery.service;

public class NoSuchTaxException extends RuntimeException {
    public NoSuchTaxException(String message) {
        super(message);
    }
}
