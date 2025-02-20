package com.thoughtworks.newsletter.service;

import com.thoughtworks.newsletter.domain.model.Newsletter;
import com.thoughtworks.newsletter.domain.model.SelectedAudienceStatus;
import com.thoughtworks.newsletter.domain.repository.SelectedAudienceStatusRepository;
import com.thoughtworks.newsletter.scheduler.dto.SelectedAudienceStatusCsvDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SelectedAudienceStatusService {

    private final SelectedAudienceStatusRepository repository;
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
}
