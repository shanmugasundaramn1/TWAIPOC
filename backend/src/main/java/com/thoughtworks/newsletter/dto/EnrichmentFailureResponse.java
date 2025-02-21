package com.thoughtworks.newsletter.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class EnrichmentFailureResponse {
    private final Map<String, Long> failureReasonCounts;
}
