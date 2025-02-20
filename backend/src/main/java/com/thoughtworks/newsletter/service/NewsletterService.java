package com.thoughtworks.newsletter.service;

import com.thoughtworks.newsletter.domain.model.Newsletter;
import com.thoughtworks.newsletter.domain.repository.NewsletterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsletterService {

    private final NewsletterRepository repository;

    @Transactional
    public Newsletter getOrCreateNewsletter(Long newsletterId) {
        Newsletter newsletter = repository.findByNewsletterId(newsletterId);
        if (newsletter != null) {
            return newsletter;
        }

        log.info("Creating new newsletter with ID: {}", newsletterId);
        newsletter = Newsletter.builder()
                .newsletterId(newsletterId)
                .newsletterName("Newsletter " + newsletterId)
                .partnerName("Partner " + newsletterId)
                .palDate(LocalDate.now())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return repository.save(newsletter);
    }
}
