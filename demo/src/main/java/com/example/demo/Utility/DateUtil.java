package com.example.demo.Utility;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    public static String getCurrentDate() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return currentDate.format(formatter);
    }
}