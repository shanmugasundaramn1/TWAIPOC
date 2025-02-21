package com.thoughtworks.newsletter.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "member_interactions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberInteraction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_uuid", nullable = false)
    private UUID memberUuid;

    @Column(name = "newsletter_id", nullable = false)
    private Long newsletterId;

    @Column(name = "delivery_timestamp")
    private Long deliveryTimestamp;

    @Column(name = "open_timestamp")
    private Long openTimestamp;

    @Column(name = "coupon_click_timestamp")
    private Long couponClickTimestamp;
}
