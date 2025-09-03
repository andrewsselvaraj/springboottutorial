package com.usermgmt.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Marking this class as a REST controller
@RestController
public class HelloWorldController { 

    // Mapping the root URL ("/") to this method
    @RequestMapping("/helloAPI") 
    public String helloWorld() { 
        
        // Returning a simple "Hello World" response
        return "I return the value from helloAPI "; 
    } 
}