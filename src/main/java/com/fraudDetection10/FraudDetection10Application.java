package com.fraudDetection10;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FraudDetection10Application {

    public static void main(String[] args) {
        SpringApplication.run(FraudDetection10Application.class, args);
    }
}