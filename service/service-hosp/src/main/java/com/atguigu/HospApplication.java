package com.atguigu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@SpringBootApplication
//@ComponentScan(basePackages = {"com.atguigu"})
@EnableFeignClients(value = "com.atguigu")
public class HospApplication {
    public static void main(String[] args) {
        SpringApplication.run(HospApplication.class, args);
    }
}