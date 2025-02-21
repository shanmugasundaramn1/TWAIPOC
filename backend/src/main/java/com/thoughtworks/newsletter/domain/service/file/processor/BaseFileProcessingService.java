package com.thoughtworks.newsletter.domain.service.file.processor;

import com.thoughtworks.newsletter.domain.service.file.csv.CsvFileProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;

public abstract class BaseFileProcessingService<T, ID> {
    private static final Logger logger = LoggerFactory.getLogger(BaseFileProcessingService.class);

    protected final FileProcessor fileProcessor;
    protected final CsvFileProcessor<T> csvProcessor;
    protected final JpaRepository<T, ID> repository;

    protected BaseFileProcessingService(
            FileProcessor fileProcessor,
            CsvFileProcessor<T> csvProcessor,
            JpaRepository<T, ID> repository) {
        this.fileProcessor = fileProcessor;
        this.csvProcessor = csvProcessor;
        this.repository = repository;
    }

    protected void processFiles() {
        try {
            List<File> unprocessedFiles = fileProcessor.listUnprocessedFiles();
            logger.info("Found {} unprocessed files", unprocessedFiles.size());

            for (File file : unprocessedFiles) {
                processFile(file);
            }
        } catch (IOException e) {
            logger.error("Error processing files", e);
        }
    }

    private void processFile(File file) throws IOException {
        try {
            logger.info("Processing file: {}", file.getName());
            List<T> entities = csvProcessor.processCsvFile(file);
            
            repository.saveAll(entities);
            logger.info("Saved {} entities from file {}", entities.size(), file.getName());
            
            fileProcessor.markFileAsProcessed(file);
            logger.info("Marked file as processed: {}", file.getName());
        } catch (IOException e) {
            logger.error("Error processing file: {}", file.getName(), e);
            throw e;
        }
    }
}
