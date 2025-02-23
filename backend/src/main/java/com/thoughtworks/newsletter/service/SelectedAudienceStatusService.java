package com.thoughtworks.newsletter.service;

import com.thoughtworks.newsletter.model.Newsletter;
import com.thoughtworks.newsletter.model.SelectedAudienceStatus;
import com.thoughtworks.newsletter.repository.SelectedAudienceStatusRepository;
import com.thoughtworks.newsletter.repository.MemberRepository;
import com.thoughtworks.newsletter.scheduler.dto.SelectedAudienceStatusCsvDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.thoughtworks.newsletter.dto.TotalTargetedResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import com.thoughtworks.newsletter.dto.InteractionCountsDto;
import com.thoughtworks.newsletter.repository.MemberInteractionRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class SelectedAudienceStatusService {

    private final SelectedAudienceStatusRepository repository;
    private final MemberRepository memberRepository;
    private final MemberInteractionRepository memberInteractionRepository;
    private final NewsletterService newsletterService;

    @Transactional
    public void processAndSaveAudienceStatus(List<SelectedAudienceStatusCsvDto> records) {
        log.info("Processing {} audience status records", records.size());

        for (SelectedAudienceStatusCsvDto record : records) {
            try {
                SelectedAudienceStatus status = convertToEntity(record);
                repository.save(status);
                log.debug("Saved audience status for newsletter {} on date {}",
                        record.getNewsletterId(), record.getDate());
            } catch (Exception e) {
                log.error("Error processing record for newsletter {}: {}",
                        record.getNewsletterId(), e.getMessage(), e);
            }
        }
    }

    private SelectedAudienceStatus convertToEntity(SelectedAudienceStatusCsvDto dto) {
        Newsletter newsletter = newsletterService.getOrCreateNewsletter(dto.getNewsletterId());

        return SelectedAudienceStatus.builder()
                .newsletter(newsletter)
                .date(LocalDate.parse(dto.getDate()))
                .potentialCount(Integer.parseInt(dto.getPotentialCount()))
                .potentialSelectedCount(Integer.parseInt(dto.getPotentialSelectedCount()))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .version(1)
                .build();
    }

    public TotalTargetedResponse getTotalTargeted(String newsletterName, LocalDate palDate, String partnerName) {
        // Get total targeted count from existing method
        Long totalTargeted = repository.countByNewsletterFilters(newsletterName, palDate, partnerName);
        
        // Get interaction counts
        InteractionCountsDto interactionCounts = memberInteractionRepository.getInteractionCounts(newsletterName, palDate, partnerName);
        
        // Get enriched count
        Long enrichedCount = memberRepository.getEnrichedCount(newsletterName, palDate, partnerName);
        
        return new TotalTargetedResponse(
            totalTargeted,
            interactionCounts.getDelivered() == null ? 0L : interactionCounts.getDelivered(),
            interactionCounts.getOpened() == null ? 0L : interactionCounts.getOpened(),
            enrichedCount == null ? 0L : enrichedCount,
            interactionCounts.getCouponClicked() == null ? 0L : interactionCounts.getCouponClicked(),
            interactionCounts.getBounced() == null ? 0L : interactionCounts.getBounced()
        );
    }
}
