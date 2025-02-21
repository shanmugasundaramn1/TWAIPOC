package com.thoughtworks.newsletter.controller;

import com.thoughtworks.newsletter.dto.TotalTargetedResponse;
import com.thoughtworks.newsletter.service.SelectedAudienceStatusService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SelectedAudienceStatusController.class)  // Specify the controller to test
public class SelectedAudienceStatusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SelectedAudienceStatusService selectedAudienceStatusService;

    @Test
    void shouldReturnTotalTargetedCount() throws Exception {
        // Given
        String newsletterName = "Test Newsletter";
        LocalDate palDate = LocalDate.of(2024, 3, 20);
        String partnerName = "Test Partner";
        TotalTargetedResponse expectedResponse = new TotalTargetedResponse(100L, 80L, 50L, 70L);
        
        when(selectedAudienceStatusService.getTotalTargeted(newsletterName, palDate, partnerName))
                .thenReturn(expectedResponse);

        // When/Then
        mockMvc.perform(get("/api/selected-audience-status/total-targeted")
                .param("newsletterName", newsletterName)
                .param("palDate", palDate.toString())
                .param("partnerName", partnerName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total_targeted").value(100))
                .andExpect(jsonPath("$.total_delivered").value(80))
                .andExpect(jsonPath("$.total_opened").value(50))
                .andExpect(jsonPath("$.data_enriched").value(70));
    }

    @Test
    void shouldReturnTotalTargetedCountWithNullParameters() throws Exception {
        // Given
        TotalTargetedResponse expectedResponse = new TotalTargetedResponse(50L, 40L, 30L, 35L);
        when(selectedAudienceStatusService.getTotalTargeted(null, null, null))
                .thenReturn(expectedResponse);

        // When/Then
        mockMvc.perform(get("/api/selected-audience-status/total-targeted"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total_targeted").value(50))
                .andExpect(jsonPath("$.total_delivered").value(40))
                .andExpect(jsonPath("$.total_opened").value(30))
                .andExpect(jsonPath("$.data_enriched").value(35));
    }

    @Test
    void shouldReturnTotalTargetedWithAllCounts() throws Exception {
        // Given
        String newsletterName = "Test Newsletter";
        LocalDate date = LocalDate.of(2024, 3, 20);
        String partnerName = "Test Partner";
        
        TotalTargetedResponse expectedResponse = new TotalTargetedResponse(10L, 8L, 5L, 7L);
        
        when(selectedAudienceStatusService.getTotalTargeted(newsletterName, date, partnerName))
                .thenReturn(expectedResponse);

        // When/Then
        mockMvc.perform(get("/api/selected-audience-status/total-targeted")
                .param("newsletterName", newsletterName)
                .param("palDate", date.toString())
                .param("partnerName", partnerName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total_targeted").value(10))
                .andExpect(jsonPath("$.total_delivered").value(8))
                .andExpect(jsonPath("$.total_opened").value(5))
                .andExpect(jsonPath("$.data_enriched").value(7));
    }
} 