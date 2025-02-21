package com.thoughtworks.newsletter.domain.service.memberinteraction;

import com.thoughtworks.newsletter.model.MemberInteraction;
import com.thoughtworks.newsletter.repository.MemberInteractionRepository;
import com.thoughtworks.newsletter.domain.service.file.csv.CsvFileProcessor;
import com.thoughtworks.newsletter.domain.service.file.processor.BaseFileProcessingService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@EnableScheduling
@Slf4j
public class MemberInteractionsLoaderService extends BaseFileProcessingService<MemberInteraction, Long> {

    public MemberInteractionsLoaderService(
            MemberInteractionFileProcessor fileProcessor,
            CsvFileProcessor<MemberInteraction> csvProcessor,
            MemberInteractionRepository repository) {
        super(fileProcessor, csvProcessor, repository);
    }

    @Scheduled(cron = "${member-interactions.processing.cron}")
    @Transactional
    public void loadMemberInteractions() {
        log.info("Processing member interactions files from scheduler..");
        processFiles();
    }
}
