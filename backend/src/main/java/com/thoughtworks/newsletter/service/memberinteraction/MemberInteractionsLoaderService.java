package com.thoughtworks.newsletter.service.memberinteraction;

import com.thoughtworks.newsletter.model.MemberInteraction;
import com.thoughtworks.newsletter.repository.MemberInteractionRepository;
import com.thoughtworks.newsletter.service.file.csv.CsvFileProcessor;
import com.thoughtworks.newsletter.service.file.processor.BaseFileProcessingService;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class MemberInteractionsLoaderService extends BaseFileProcessingService<MemberInteraction, Long> {

    public MemberInteractionsLoaderService(
            MemberInteractionFileProcessor fileProcessor,
            CsvFileProcessor<MemberInteraction> csvProcessor,
            MemberInteractionRepository repository) {
        super(fileProcessor, csvProcessor, repository);
    }

    @Scheduled(fixedRate = 5, timeUnit = TimeUnit.MINUTES)
    @Transactional
    public void loadMemberInteractions() {
        log.info("Processing member interactions files from scheduler..");
        processFiles();
    }
}
