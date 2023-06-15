package com.atguigu.syt.yun;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)//取消数据源配置自动读取
@ComponentScan(basePackages = {"com.atguigu"})
public class ServiceYunApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceYunApplication.class, args);
    }
}