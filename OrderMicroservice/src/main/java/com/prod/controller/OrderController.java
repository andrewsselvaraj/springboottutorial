package com.prod.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    @GetMapping("/order/status")
    public String getOrderStatus() {
        return "Order Service is running successfully!";
    }
}
