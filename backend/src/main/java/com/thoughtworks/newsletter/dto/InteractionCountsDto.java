package com.thoughtworks.newsletter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InteractionCountsDto {
    private Long delivered;
    private Long opened;
} 