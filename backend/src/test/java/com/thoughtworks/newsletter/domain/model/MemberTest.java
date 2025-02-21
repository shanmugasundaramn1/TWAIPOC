package com.thoughtworks.newsletter.domain.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class MemberTest {

    @Test
    void shouldCreateValidMember() {
        // Given
        Member member = Member.builder()
                .newsletterId(1L)
                .memberId(UUID.randomUUID())
                .status(EnrichmentStatus.ENRICHED)
                .fileName("test-file.csv")
                .processedAt(LocalDateTime.now())
                .build();

        // Then
        assertThat(member.getNewsletterId()).isEqualTo(1L);
        assertThat(member.getStatus()).isEqualTo(EnrichmentStatus.ENRICHED);
        assertThat(member.getFileName()).isEqualTo("test-file.csv");
        assertThat(member.getProcessedAt()).isNotNull();
    }

    @Test
    void shouldSetAuditFieldsOnCreate() {
        // Given
        Member member = Member.builder()
                .newsletterId(1L)
                .memberId(UUID.randomUUID())
                .status(EnrichmentStatus.ENRICHED)
                .fileName("test-file.csv")
                .processedAt(LocalDateTime.now())
                .build();

        // When
        member.onCreate();

        // Then
        assertThat(member.getCreatedAt()).isNotNull();
        assertThat(member.getUpdatedAt()).isNotNull();
        assertThat(member.getCreatedAt()).isEqualTo(member.getUpdatedAt());
    }

    @Test
    void shouldUpdateTimestampOnUpdate() {
        // Given
        Member member = Member.builder()
                .newsletterId(1L)
                .memberId(UUID.randomUUID())
                .status(EnrichmentStatus.ENRICHED)
                .fileName("test-file.csv")
                .processedAt(LocalDateTime.now())
                .build();
        member.onCreate();
        LocalDateTime originalUpdatedAt = member.getUpdatedAt();

        // When
        member.setStatus(EnrichmentStatus.FAILED);
        member.setErrorMessage("Test error");
        member.onUpdate();

        // Then
        assertThat(member.getUpdatedAt()).isAfter(originalUpdatedAt);
        assertThat(member.getCreatedAt()).isBefore(member.getUpdatedAt());
    }

    @Test
    void shouldValidateRequiredFields() {
        // Given
        Member member = new Member();

        // Then
        assertThat(member.getNewsletterId()).isNull();
        assertThat(member.getMemberId()).isNull();
        assertThat(member.getStatus()).isNull();
        assertThat(member.getFileName()).isNull();
        assertThat(member.getProcessedAt()).isNull();
    }

    @Test
    void shouldHandleErrorMessage() {
        // Given
        Member member = Member.builder()
                .newsletterId(1L)
                .memberId(UUID.randomUUID())
                .status(EnrichmentStatus.FAILED)
                .fileName("test-file.csv")
                .processedAt(LocalDateTime.now())
                .errorMessage("Processing failed")
                .build();

        // Then
        assertThat(member.getErrorMessage()).isEqualTo("Processing failed");
        assertThat(member.getStatus()).isEqualTo(EnrichmentStatus.FAILED);
    }
}
