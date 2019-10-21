package com.citi.group2.simpletps;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.citi.group2.simpletps.mapper")
public class SimpleTpsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimpleTpsApplication.class, args);
    }

}
