package com.OtpApp.OtpApplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OtpApplication {

    public static void main(String[] args) {
        SpringApplication.run(OtpApplication.class, args);
    }

}
