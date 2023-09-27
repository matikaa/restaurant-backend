package com.restaurant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"configuration", "com.restaurant"})
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
