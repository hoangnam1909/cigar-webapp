package com.nhn.cigarwebapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class CigarWebappApplication {

    @GetMapping("/hello")
    public String hello() {
        return "Hello Hoang Nam day!!!";
    }

    public static void main(String[] args) {
        SpringApplication.run(CigarWebappApplication.class, args);
    }

}
