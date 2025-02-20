package com.thoughtworks.newsletter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NewsletterDashboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewsletterDashboardApplication.class, args);
    }
}
