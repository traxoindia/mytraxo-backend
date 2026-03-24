package com.mytraxo;

import com.mytraxo.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling 

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)  // 👈 ADD THIS LINE
public class MyTraxoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyTraxoApplication.class, args);
    }
}