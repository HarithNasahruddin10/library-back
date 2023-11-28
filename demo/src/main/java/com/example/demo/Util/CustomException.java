package com.example.demo.Util;


import org.springframework.web.bind.annotation.ExceptionHandler;

public class CustomException extends Exception {

    public CustomException(String message) {
        super(message);
    }
}
