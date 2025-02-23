package com.thoughtworks.newsletter.repository;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import com.thoughtworks.newsletter.model.Newsletter;
import com.thoughtworks.newsletter.model.MemberInteraction;
import com.thoughtworks.newsletter.dto.InteractionCountsDto;

@Disabled("Disabled until TestContainers setup is complete")
@DataJpaTest
class MemberInteractionRepositoryTest {
    @Autowired
    private MemberInteractionRepository memberInteractionRepository;
    
    @Autowired
    private TestEntityManager entityManager;

    @Test
    void shouldCountInteractionsBasedOnTimestamps() {
        // Given
        Newsletter newsletter = new Newsletter();
        newsletter.setNewsletterName("Test Newsletter");
        newsletter.setPalDate(LocalDate.of(2024, 3, 20));
        newsletter.setPartnerName("Test Partner");
        entityManager.persist(newsletter);

        // Create member interactions
        MemberInteraction delivered = new MemberInteraction();
        delivered.setNewsletterId(newsletter.getId());
        delivered.setDeliveryTimestamp(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        entityManager.persist(delivered);

        MemberInteraction openedAndClicked = new MemberInteraction();
        openedAndClicked.setNewsletterId(newsletter.getId());
        openedAndClicked.setDeliveryTimestamp(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        openedAndClicked.setOpenTimestamp(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        openedAndClicked.setCouponClickTimestamp(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        entityManager.persist(openedAndClicked);

        MemberInteraction noInteraction = new MemberInteraction();
        noInteraction.setNewsletterId(newsletter.getId());
        entityManager.persist(noInteraction);

        entityManager.flush();

        // When
        InteractionCountsDto counts = memberInteractionRepository.getInteractionCounts(
            "Test Newsletter",
            LocalDate.of(2024, 3, 20),
            "Test Partner"
        );

        // Then
        assertEquals(2L, counts.getDelivered());    // 2 with delivery timestamp
        assertEquals(1L, counts.getOpened());       // 1 with open timestamp
        assertEquals(1L, counts.getCouponClicked()); // 1 with coupon click timestamp
        assertEquals(1L, counts.getBounced());      // 3 total - 2 delivered = 1 bounced
    }
}
