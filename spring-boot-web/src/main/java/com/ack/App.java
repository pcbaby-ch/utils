package com.ack;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
//import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication(scanBasePackages={"com.ack.*"},exclude = {DataSourceAutoConfiguration.class})
@MapperScan("com.ack.mappers")
@EnableTransactionManagement
//@EnableDiscoveryClient
//@EnableFeignClients
public class App {

    private final static Log logger = LogFactory.getLog(App.class);

    public static void main(String[] args) {
    	logger.info("App启动开始！");
        SpringApplication.run(App.class, args);
        logger.info("App启动结束！");
    }
    
}
