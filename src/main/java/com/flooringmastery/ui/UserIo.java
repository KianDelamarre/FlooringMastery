package com.flooringmastery.ui;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface UserIo {
    void print(String message);

    String readString(String prompt);

    String readString(String prompt, List<String> options);

    int readInt(String prompt);

    int readInt(String prompt, int min, int max);


    double readDouble(String prompt);

    double readDouble(String prompt, double min, double max);

    float readFloat(String prompt);

    float readFloat(String prompt, float min, float max);

    long readLong(String prompt);

    long readLong(String prompt, long min, long max);

    BigDecimal readBigDecimal(String prompt);

    LocalDate readLocalDate(String prompt);
}
