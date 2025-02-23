package com.thoughtworks.newsletter.controller;

import com.thoughtworks.newsletter.dto.EnrichmentFailureResponse;
import com.thoughtworks.newsletter.service.EnrichmentFailureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/enrichment/failures")
@RequiredArgsConstructor
@Tag(name = "Enrichment Failures", description = "API to get statistics about failed enrichments")
@Validated
public class EnrichmentFailureController {
    private final EnrichmentFailureService enrichmentFailureService;

    @Operation(
            summary = "Get failure statistics",
            description = "Retrieve statistics about failed enrichments grouped by failure reason"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved failure statistics"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters")
    })
    @GetMapping
    public ResponseEntity<EnrichmentFailureResponse> getFailureStats(
            @Parameter(description = "Name of the newsletter", required = true)
            @RequestParam(required = true) String newsletterName,
            @Parameter(description = "Name of the partner", required = true)
            @RequestParam(required = true) String partnerName,
            @Parameter(description = "PAL date in format YYYY-MM-DD", required = true)
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate palDate) {
        var failureStats = enrichmentFailureService.getFailureReasonStats(
                newsletterName,
                partnerName,
                palDate
        );
        return ResponseEntity.ok(new EnrichmentFailureResponse(failureStats));
    }
}
