package com.nhn.cigarwebapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import redis.clients.jedis.Jedis;


@SpringBootApplication
@EnableCaching
@EnableAsync
public class CigarWebappApplication implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(CigarWebappApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(CigarWebappApplication.class, args);
    }

    @Value("${settings.cors_origin}")
    private String FRONTEND_DOMAIN;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(FRONTEND_DOMAIN)
                        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE");
            }
        };
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            Jedis jedis = new Jedis();
            jedis.flushAll();
            logger.info("FlushAll Redis");
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }
}
