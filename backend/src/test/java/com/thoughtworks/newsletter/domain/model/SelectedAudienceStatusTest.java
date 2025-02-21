package com.thoughtworks.newsletter.domain.model;

import com.thoughtworks.newsletter.config.BaseIntegrationTest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Disabled;

@Disabled
class SelectedAudienceStatusTest extends BaseIntegrationTest {

    @Autowired
    private EntityManager entityManager;

    @Test
    void shouldPersistSelectedAudienceStatus() {
        // Given
        Newsletter newsletter = createNewsletter();
        entityManager.persist(newsletter);

        SelectedAudienceStatus status = SelectedAudienceStatus.builder()
                .newsletter(newsletter)
                .date(LocalDate.now())
                .potentialCount(100)
                .potentialSelectedCount(50)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // When
        entityManager.persist(status);
        entityManager.flush();
        entityManager.clear();

        // Then
        SelectedAudienceStatus found = entityManager.find(SelectedAudienceStatus.class, status.getId());
        assertThat(found).isNotNull();
        assertThat(found.getNewsletter().getId()).isEqualTo(newsletter.getId());
        assertThat(found.getPotentialCount()).isEqualTo(100);
        assertThat(found.getPotentialSelectedCount()).isEqualTo(50);
        assertThat(found.getVersion()).isEqualTo(1);
    }

    @Test
    void shouldEnforceUniqueConstraint() {
        // Given
        Newsletter newsletter = createNewsletter();
        entityManager.persist(newsletter);

        LocalDate date = LocalDate.now();
        SelectedAudienceStatus status1 = SelectedAudienceStatus.builder()
                .newsletter(newsletter)
                .date(date)
                .potentialCount(100)
                .potentialSelectedCount(50)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        SelectedAudienceStatus status2 = SelectedAudienceStatus.builder()
                .newsletter(newsletter)
                .date(date)
                .potentialCount(200)
                .potentialSelectedCount(100)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // When/Then
        entityManager.persist(status1);
        entityManager.flush();

        assertThatThrownBy(() -> {
            entityManager.persist(status2);
            entityManager.flush();
        }).isInstanceOf(PersistenceException.class)
                .hasMessageContaining("uk_newsletter_date");
    }

    @Test
    void shouldIncrementVersionOnUpdate() {
        // Given
        Newsletter newsletter = createNewsletter();
        entityManager.persist(newsletter);

        SelectedAudienceStatus status = SelectedAudienceStatus.builder()
                .newsletter(newsletter)
                .date(LocalDate.now())
                .potentialCount(100)
                .potentialSelectedCount(50)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        entityManager.persist(status);
        entityManager.flush();
        entityManager.clear();

        // When
        SelectedAudienceStatus found = entityManager.find(SelectedAudienceStatus.class, status.getId());
        found.setPotentialCount(150);
        entityManager.flush();
        entityManager.clear();

        // Then
        SelectedAudienceStatus updated = entityManager.find(SelectedAudienceStatus.class, status.getId());
        assertThat(updated.getVersion()).isEqualTo(2);
        assertThat(updated.getPotentialCount()).isEqualTo(150);
    }

    private Newsletter createNewsletter() {
        return Newsletter.builder()
                .newsletterId(1L)
                .newsletterName("Test Newsletter")
                .partnerName("Test Partner")
                .palDate(LocalDate.now())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
