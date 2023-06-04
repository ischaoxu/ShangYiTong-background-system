package com.atguigu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
@EnableDiscoveryClient
@SpringBootApplication
//@ComponentScan(basePackages = {"com.atguigu"})
public class HospApplication {
    public static void main(String[] args) {
        SpringApplication.run(HospApplication.class, args);
    }
}