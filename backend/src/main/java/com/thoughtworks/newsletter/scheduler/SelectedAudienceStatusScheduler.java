package com.thoughtworks.newsletter.scheduler;

import com.thoughtworks.newsletter.config.NewsletterProperties;
import com.thoughtworks.newsletter.scheduler.dto.SelectedAudienceStatusCsvDto;
import com.thoughtworks.newsletter.service.SelectedAudienceStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class SelectedAudienceStatusScheduler {

    private final NewsletterProperties properties;
    private final CsvFileProcessor csvFileProcessor;
    private final SelectedAudienceStatusService service;

    @Scheduled(fixedRate = 5, timeUnit = TimeUnit.MINUTES)
    public void processAudienceStatusFiles() {
        try {
            String csvPath = properties.getScheduler().getCsv().getPath();
            log.info("Starting to process CSV files from path: {}", csvPath);

            File directory = new File(csvPath);
            if (!directory.exists() || !directory.isDirectory()) {
                log.error("CSV directory does not exist or is not a directory: {}", csvPath);
                return;
            }

            File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".csv"));
            if (files == null || files.length == 0) {
                log.info("No CSV files found in directory: {}", csvPath);
                return;
            }

            for (File file : files) {
                try {
                    log.info("Processing file: {}", file.getName());
                    List<SelectedAudienceStatusCsvDto> records = csvFileProcessor.processCsvFile(file);
                    service.processAndSaveAudienceStatus(records);

                    // Delete the processed file
                    if (!file.delete()) {
                        log.warn("Could not delete processed file: {}", file.getName());
                    }
                } catch (Exception e) {
                    log.error("Error processing file {}: {}", file.getName(), e.getMessage(), e);
                }
            }
        } catch (Exception e) {
            log.error("Error in scheduler execution: {}", e.getMessage(), e);
        }
    }
}
