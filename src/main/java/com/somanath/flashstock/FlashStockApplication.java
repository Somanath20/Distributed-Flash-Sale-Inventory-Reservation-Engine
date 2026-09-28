package com.somanath.flashstock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FlashStockApplication {
    public static void main(String[] args) { SpringApplication.run(FlashStockApplication.class, args); }
}
