package com.thoughtworks.newsletter.config;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {
    // Empty base class - configuration is handled by annotations
}
