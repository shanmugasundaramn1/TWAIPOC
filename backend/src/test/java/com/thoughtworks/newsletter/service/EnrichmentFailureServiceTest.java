package com.thoughtworks.newsletter.service;

import com.thoughtworks.newsletter.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EnrichmentFailureServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private EnrichmentFailureService enrichmentFailureService;

    @Test
    void shouldReturnFailureReasonStatsWhenDataExists() {
        // Given
        String newsletterName = "Test Newsletter";
        String partnerName = "Test Partner";
        LocalDate palDate = LocalDate.of(2025, 2, 21);

        Object[] stat1 = new Object[]{"Invalid email", 5L};
        Object[] stat2 = new Object[]{"Missing phone", 3L};
        when(memberRepository.getFailureReasonStats(newsletterName, partnerName, palDate))
                .thenReturn(Arrays.asList(stat1, stat2));

        // When
        Map<String, Long> result = enrichmentFailureService.getFailureReasonStats(newsletterName, partnerName, palDate);

        // Then
        assertThat(result)
                .hasSize(2)
                .containsEntry("Invalid email", 5L)
                .containsEntry("Missing phone", 3L);
    }

    @Test
    void shouldReturnEmptyMapWhenNoFailuresExist() {
        // Given
        String newsletterName = "Test Newsletter";
        String partnerName = "Test Partner";
        LocalDate palDate = LocalDate.of(2025, 2, 21);

        when(memberRepository.getFailureReasonStats(newsletterName, partnerName, palDate))
                .thenReturn(Collections.emptyList());

        // When
        Map<String, Long> result = enrichmentFailureService.getFailureReasonStats(newsletterName, partnerName, palDate);

        // Then
        assertThat(result).isEmpty();
    }
}
