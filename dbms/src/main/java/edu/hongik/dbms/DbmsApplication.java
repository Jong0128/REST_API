package edu.hongik.dbms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "edu.hongik.dbms")
public class DbmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(DbmsApplication.class, args);
    }
}