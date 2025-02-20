package com.thoughtworks.newsletter.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@EnableConfigurationProperties(NewsletterProperties.class)
public class SchedulerConfig {
    // Configuration is handled through annotations
    // Additional beans can be added here if needed
}
