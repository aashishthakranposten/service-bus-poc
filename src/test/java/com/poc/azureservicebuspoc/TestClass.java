package com.poc.azureservicebuspoc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestClass {

    public static void main(String[] args) {
        String str = "2022-03-16-01.34.25.004917";
        LocalDateTime date = LocalDateTime.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd-HH.mm.ss.SSSSSS"));
        System.out.println(date);
    }
}
