package com.thoughtworks.newsletter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InteractionCountsDto {
    private Long delivered;
    private Long opened;
    private Long couponClicked;
    private Long bounced;
}
