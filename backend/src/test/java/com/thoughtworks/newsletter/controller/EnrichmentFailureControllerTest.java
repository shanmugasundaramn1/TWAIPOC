package com.thoughtworks.newsletter.controller;

import com.thoughtworks.newsletter.exception.GlobalExceptionHandler;
import com.thoughtworks.newsletter.service.EnrichmentFailureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class EnrichmentFailureControllerTest {

    @Mock
    private EnrichmentFailureService enrichmentFailureService;

    @InjectMocks
    private EnrichmentFailureController enrichmentFailureController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(enrichmentFailureController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void shouldReturnFailureStatsWhenValidRequest() throws Exception {
        // Given
        Map<String, Long> failureStats = new HashMap<>();
        failureStats.put("Invalid email", 5L);
        failureStats.put("Missing phone", 3L);

        String newsletterName = "Test Newsletter";
        String partnerName = "Test Partner";
        LocalDate palDate = LocalDate.of(2025, 2, 21);

        when(enrichmentFailureService.getFailureReasonStats(newsletterName, partnerName, palDate))
                .thenReturn(failureStats);

        // When & Then
        mockMvc.perform(get("/api/v1/enrichment/failures")
                        .param("newsletterName", "Test Newsletter")
                        .param("partnerName", "Test Partner")
                        .param("palDate", "2025-02-21")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.failureReasonCounts['Invalid email']").value(5))
                .andExpect(jsonPath("$.failureReasonCounts['Missing phone']").value(3));
    }

//    @Test
//    void shouldReturnBadRequestWhenMissingRequiredParams() throws Exception {
//        // When & Then
//        mockMvc.perform(get("/api/v1/enrichment/failures")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andDo(print())
//                .andExpect(jsonPath("$.newsletterName").value("Newsletter name is required"))
//                .andExpect(jsonPath("$.partnerName").value("Partner name is required"))
//                .andExpect(jsonPath("$.palDate").value("PAL date is required"));
//    }

    @Test
    void shouldReturnBadRequestWhenInvalidDateFormat() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/enrichment/failures")
                        .param("newsletterName", "Test Newsletter")
                        .param("partnerName", "Test Partner")
                        .param("palDate", "invalid-date")
                        .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.palDate").value("Invalid value provided"));
    }
}
