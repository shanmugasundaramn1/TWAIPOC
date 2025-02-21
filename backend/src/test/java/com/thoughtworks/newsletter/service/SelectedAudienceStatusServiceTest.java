package com.thoughtworks.newsletter.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import com.thoughtworks.newsletter.repository.SelectedAudienceStatusRepository;
import com.thoughtworks.newsletter.dto.TotalTargetedResponse;
import com.thoughtworks.newsletter.dto.InteractionCountsDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.thoughtworks.newsletter.service.SelectedAudienceStatusService;
import com.thoughtworks.newsletter.repository.MemberRepository;
import com.thoughtworks.newsletter.repository.MemberInteractionRepository;

@ExtendWith(MockitoExtension.class)
class SelectedAudienceStatusServiceTest {

    @Mock
    private SelectedAudienceStatusRepository selectedAudienceStatusRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MemberInteractionRepository memberInteractionRepository;

    @InjectMocks
    private SelectedAudienceStatusService selectedAudienceStatusService;

    @Test
    void shouldReturnTotalTargetedCount() {
        // Given
        String newsletterName = "Test Newsletter";
        LocalDate date = LocalDate.of(2024, 3, 20);
        String partnerName = "Test Partner";
        
        when(selectedAudienceStatusRepository.countByNewsletterFilters(newsletterName, date, partnerName))
                .thenReturn(10L);
        when(memberInteractionRepository.getInteractionCounts(newsletterName, date, partnerName))
                .thenReturn(new InteractionCountsDto(0L, 0L));
        when(memberRepository.getEnrichedCount(newsletterName, date, partnerName))
                .thenReturn(0L);

        // When
        TotalTargetedResponse response = selectedAudienceStatusService.getTotalTargeted(newsletterName, date, partnerName);

        // Then
        assertEquals(10L, response.getTotal_targeted());
        assertEquals(0L, response.getTotal_delivered());
        assertEquals(0L, response.getTotal_opened());
        assertEquals(0L, response.getData_enriched());
        verify(selectedAudienceStatusRepository).countByNewsletterFilters(newsletterName, date, partnerName);
        verify(memberInteractionRepository).getInteractionCounts(newsletterName, date, partnerName);
        verify(memberRepository).getEnrichedCount(newsletterName, date, partnerName);
    }

    @Test
    void shouldReturnTotalTargetedWithInteractionCounts() {
        // Given
        String newsletterName = "Test Newsletter";
        LocalDate date = LocalDate.of(2024, 3, 20);
        String partnerName = "Test Partner";
        
        when(selectedAudienceStatusRepository.countByNewsletterFilters(newsletterName, date, partnerName))
                .thenReturn(10L);
        when(memberInteractionRepository.getInteractionCounts(newsletterName, date, partnerName))
                .thenReturn(new InteractionCountsDto(8L, 5L));

        // When
        TotalTargetedResponse response = selectedAudienceStatusService.getTotalTargeted(newsletterName, date, partnerName);

        // Then
        assertEquals(10L, response.getTotal_targeted());
        assertEquals(8L, response.getTotal_delivered());
        assertEquals(5L, response.getTotal_opened());
        verify(selectedAudienceStatusRepository).countByNewsletterFilters(newsletterName, date, partnerName);
        verify(memberInteractionRepository).getInteractionCounts(newsletterName, date, partnerName);
    }

    @Test
    void shouldReturnTotalTargetedWithInteractionAndEnrichedCounts() {
        // Given
        String newsletterName = "Test Newsletter";
        LocalDate date = LocalDate.of(2024, 3, 20);
        String partnerName = "Test Partner";
        
        when(selectedAudienceStatusRepository.countByNewsletterFilters(newsletterName, date, partnerName))
                .thenReturn(10L);
        when(memberInteractionRepository.getInteractionCounts(newsletterName, date, partnerName))
                .thenReturn(new InteractionCountsDto(8L, 5L));
        when(memberRepository.getEnrichedCount(newsletterName, date, partnerName))
                .thenReturn(7L);

        // When
        TotalTargetedResponse response = selectedAudienceStatusService.getTotalTargeted(newsletterName, date, partnerName);

        // Then
        assertEquals(10L, response.getTotal_targeted());
        assertEquals(8L, response.getTotal_delivered());
        assertEquals(5L, response.getTotal_opened());
        assertEquals(7L, response.getData_enriched());
        
        verify(selectedAudienceStatusRepository).countByNewsletterFilters(newsletterName, date, partnerName);
        verify(memberInteractionRepository).getInteractionCounts(newsletterName, date, partnerName);
        verify(memberRepository).getEnrichedCount(newsletterName, date, partnerName);
    }
} 