package com.nhn.cigarwebapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class CigarWebappApplication {

    @GetMapping("/keep-server-on")
    public String hello() {
        return "Successfully!!!";
    }

    public static void main(String[] args) {
        SpringApplication.run(CigarWebappApplication.class, args);
    }

}
