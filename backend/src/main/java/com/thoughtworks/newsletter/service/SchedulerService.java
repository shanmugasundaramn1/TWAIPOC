package com.thoughtworks.newsletter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class SchedulerService {

    private final FileProcessorService fileProcessorService;

    @Scheduled(fixedDelayString = "${app.file-processor.schedule-delay:300000}")
    public void processFiles() {
        log.info("Starting scheduled file processing");
        try {
            fileProcessorService.processFiles();
        } catch (Exception e) {
            log.error("Error during scheduled file processing", e);
        }
        log.info("Completed scheduled file processing");
    }
}
