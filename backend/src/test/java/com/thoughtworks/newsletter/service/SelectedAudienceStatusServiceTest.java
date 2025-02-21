package com.thoughtworks.newsletter.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import com.thoughtworks.newsletter.repository.SelectedAudienceStatusRepository;
import com.thoughtworks.newsletter.dto.TotalTargetedResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SelectedAudienceStatusServiceTest {

    @Mock
    private SelectedAudienceStatusRepository selectedAudienceStatusRepository;

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

        // When
        TotalTargetedResponse response = selectedAudienceStatusService.getTotalTargeted(newsletterName, date, partnerName);

        // Then
        assertEquals(10L, response.getTotal_targeted());
        verify(selectedAudienceStatusRepository).countByNewsletterFilters(newsletterName, date, partnerName);
    }
} 