package com.thoughtworks.newsletter.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "newsletter")
public class NewsletterProperties {

    private final Scheduler scheduler = new Scheduler();

    @Getter
    @Setter
    public static class Scheduler {
        private final Csv csv = new Csv();

        @Getter
        @Setter
        public static class Csv {
            @NotBlank(message = "CSV file path must not be empty")
            private String path;
        }
    }
}
