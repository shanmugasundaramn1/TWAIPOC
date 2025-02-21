package com.thoughtworks.newsletter.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.thoughtworks.newsletter.service.SelectedAudienceStatusService;
import com.thoughtworks.newsletter.dto.TotalTargetedResponse;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/selected-audience-status")
public class SelectedAudienceStatusController {

    private final SelectedAudienceStatusService selectedAudienceStatusService;

    public SelectedAudienceStatusController(SelectedAudienceStatusService selectedAudienceStatusService) {
        this.selectedAudienceStatusService = selectedAudienceStatusService;
    }

    @GetMapping("/total-targeted")
    public ResponseEntity<TotalTargetedResponse> getTotalTargeted(
            @RequestParam(required = false) String newsletterName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) String partnerName
    ) {
        TotalTargetedResponse response = selectedAudienceStatusService.getTotalTargeted(newsletterName, date, partnerName);
        return ResponseEntity.ok(response);
    }
} 