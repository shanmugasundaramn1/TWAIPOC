package com.thoughtworks.newsletter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TotalTargetedResponse {
    private Long total_targeted;
    private Long total_delivered;
    private Long total_opened;
    private Long data_enriched;
}