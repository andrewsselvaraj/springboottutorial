package com.onlineexam.controller;



import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlineexam.service.FirestoreService;

import java.util.Map;

@RestController
public class HelloController {

    private final FirestoreService firestoreService;

    public HelloController(FirestoreService firestoreService) {
        this.firestoreService = firestoreService;
    }

    @GetMapping("/hello")
    public String hello() throws Exception {

        firestoreService.save(
                "hello_messages",
                "prod_msg",
                Map.of(
                    "message", "Hello World from PROD Spring Boot",
                    "env", "production"
                )
        );

        return "✅ Hello World saved using PROD credentials";
    }
}
