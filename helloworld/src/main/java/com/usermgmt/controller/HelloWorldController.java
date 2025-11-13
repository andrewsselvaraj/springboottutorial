package com.usermgmt.controller;

import java.util.Random;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.time.Duration;
import java.time.LocalTime;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;



// Marking this class as a REST controller
@RestController
public class HelloWorldController { 

    // Mapping the root URL ("/") to this method
    @RequestMapping("/helloAPI") 
    public String helloWorld() { 
        
        // Returning a simple "Hello World" response
        return "I return the value from helloAPI "; 
    }
    
    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping("/tempature") 
    public String getTemparaure() { 
        
        // Returning a simple "Hello World" response
    	Random r = new Random(27);
        return ""+r.hashCode();
    }
    
    
    @GetMapping(value = "/stream/time", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamTime() {
    	System.out.println("streamTime");
        return Flux.interval(Duration.ofMillis(500))
                   .map(seq -> "Server Time: " + LocalTime.now());
    }
    
}