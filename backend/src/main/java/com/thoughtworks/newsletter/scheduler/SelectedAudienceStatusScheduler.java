package com.thoughtworks.newsletter.scheduler;

import com.thoughtworks.newsletter.config.NewsletterProperties;
import com.thoughtworks.newsletter.scheduler.dto.SelectedAudienceStatusCsvDto;
import com.thoughtworks.newsletter.service.SelectedAudienceStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class SelectedAudienceStatusScheduler {

    private final NewsletterProperties properties;
    private final SelectedAudienceStatusCsvFileProcessor csvFileProcessor;
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

                    moveToProcessed(file.toPath());

                    // Delete the processed file
                    // if (!file.delete()) {
                    //     log.warn("Could not delete processed file: {}", file.getName());
                    // }
                } catch (Exception e) {
                    log.error("Error processing file {}: {}", file.getName(), e.getMessage(), e);
                }
            }
        } catch (Exception e) {
            log.error("Error in scheduler execution: {}", e.getMessage(), e);
        }
    }

    private void moveToProcessed(Path file) throws IOException {
        Path processedDir = Path.of(properties.getScheduler().getCsv().getPath(), "processed");
        if (!Files.exists(processedDir)) {
            Files.createDirectories(processedDir);
        }
        Path targetPath = processedDir.resolve(file.getFileName());
        Files.move(file, targetPath, StandardCopyOption.REPLACE_EXISTING);
        log.info("Moved file {} to processed directory", file.getFileName());
    }
}
