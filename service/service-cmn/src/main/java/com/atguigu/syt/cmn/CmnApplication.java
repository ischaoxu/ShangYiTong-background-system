package com.atguigu.syt.cmn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.atguigu")
public class CmnApplication {
    public static void main(String[] args) {
        SpringApplication.run(CmnApplication.class, args);
    }
}