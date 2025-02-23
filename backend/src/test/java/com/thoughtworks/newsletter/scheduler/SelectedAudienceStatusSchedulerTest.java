package com.thoughtworks.newsletter.scheduler;

import com.thoughtworks.newsletter.config.NewsletterProperties;
import com.thoughtworks.newsletter.scheduler.dto.SelectedAudienceStatusCsvDto;
import com.thoughtworks.newsletter.service.SelectedAudienceStatusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import lombok.SneakyThrows;

@ExtendWith(MockitoExtension.class)
class SelectedAudienceStatusSchedulerTest {

    @Mock
    private NewsletterProperties properties;

    @Mock
    private NewsletterProperties.Scheduler scheduler;

    @Mock
    private NewsletterProperties.Scheduler.Csv csv;

    @Mock
    private SelectedAudienceStatusCsvFileProcessor csvFileProcessor;

    @Mock
    private SelectedAudienceStatusService service;

    private SelectedAudienceStatusScheduler statusScheduler;

    @TempDir
    Path tempDir;

    private Path processedDir;

    @BeforeEach
    void setUp() throws IOException {
        when(properties.getScheduler()).thenReturn(scheduler);
        when(scheduler.getCsv()).thenReturn(csv);
        when(csv.getPath()).thenReturn(tempDir.toString());
        processedDir = tempDir.resolve("processed");
        Files.createDirectory(processedDir);
        statusScheduler = new SelectedAudienceStatusScheduler(properties, csvFileProcessor, service);
    }

    private Path createTestFile(String fileName, String content) throws IOException {
        Path targetPath = tempDir.resolve(fileName);
        Files.writeString(targetPath, content);
        return targetPath;
    }

    private void verifyFileMovedToProcessed(String fileName) throws IOException {
        assertTrue(Files.exists(processedDir.resolve(fileName)), 
            "File should exist in processed directory: " + fileName);
        assertFalse(Files.exists(tempDir.resolve(fileName)), 
            "File should not exist in source directory: " + fileName);
    }

    @Test
    void shouldProcessValidCsvFiles() throws IOException {
        // Given
        String fileName = "valid_audience_status.csv";
        createTestFile(fileName, "content");
        List<SelectedAudienceStatusCsvDto> mockRecords = List.of(new SelectedAudienceStatusCsvDto());
        when(csvFileProcessor.processCsvFile(any(File.class))).thenReturn(mockRecords);

        // When
        statusScheduler.processAudienceStatusFiles();

        // Then
        verify(csvFileProcessor).processCsvFile(any(File.class));
        verify(service).processAndSaveAudienceStatus(mockRecords);
        verifyFileMovedToProcessed(fileName);
    }

    @Test
    void shouldHandleInvalidDirectory() throws IOException {
        // Given
        when(csv.getPath()).thenReturn("/invalid/path");

        // When
        statusScheduler.processAudienceStatusFiles();

        // Then
        verify(csvFileProcessor, never()).processCsvFile(any());
        verify(service, never()).processAndSaveAudienceStatus(any());
    }

    @Test
    void shouldHandleEmptyDirectory() throws IOException {
        // When
        statusScheduler.processAudienceStatusFiles();

        // Then
        verify(csvFileProcessor, never()).processCsvFile(any());
        verify(service, never()).processAndSaveAudienceStatus(any());
    }

    @Test
    void shouldHandleProcessingError() throws IOException {
        // Given
        String fileName = "error.csv";
        createTestFile(fileName, "content");
        when(csvFileProcessor.processCsvFile(any(File.class)))
            .thenThrow(new IllegalArgumentException("Processing error"));

        // When
        statusScheduler.processAudienceStatusFiles();

        // Then
        verify(csvFileProcessor).processCsvFile(any(File.class));
        verify(service, never()).processAndSaveAudienceStatus(any());
        assertTrue(Files.exists(tempDir.resolve(fileName)), 
            "File should remain in source directory on error");
        assertFalse(Files.exists(processedDir.resolve(fileName)),
            "File should not be moved to processed directory on error");
    }

    @Test
    void shouldProcessMultipleFiles() throws IOException {
        // Given
        createTestFile("file1.csv", "content1");
        createTestFile("file2.csv", "content2");
        createTestFile("notcsv.txt", "not a csv");

        List<SelectedAudienceStatusCsvDto> mockRecords = List.of(new SelectedAudienceStatusCsvDto());
        when(csvFileProcessor.processCsvFile(any(File.class))).thenReturn(mockRecords);

        // When
        statusScheduler.processAudienceStatusFiles();

        // Then
        verify(csvFileProcessor, times(2)).processCsvFile(any(File.class));
        verify(service, times(2)).processAndSaveAudienceStatus(mockRecords);
        
        verifyFileMovedToProcessed("file1.csv");
        verifyFileMovedToProcessed("file2.csv");
        assertTrue(Files.exists(tempDir.resolve("notcsv.txt")), 
            "Non-CSV file should remain in source directory");
        assertFalse(Files.exists(processedDir.resolve("notcsv.txt")),
            "Non-CSV file should not be moved to processed directory");
    }

    @Test
    void shouldHandleProcessedDirectoryCreation() throws IOException {
        // Given
        Files.delete(processedDir);
        
        String fileName = "test.csv";
        createTestFile(fileName, "content");
        List<SelectedAudienceStatusCsvDto> mockRecords = List.of(new SelectedAudienceStatusCsvDto());
        when(csvFileProcessor.processCsvFile(any(File.class))).thenReturn(mockRecords);

        // When
        statusScheduler.processAudienceStatusFiles();

        // Then
        verify(csvFileProcessor).processCsvFile(any(File.class));
        verify(service).processAndSaveAudienceStatus(mockRecords);
        assertTrue(Files.exists(processedDir), "Processed directory should be created");
        verifyFileMovedToProcessed(fileName);
    }

    @Test
    void shouldHandleFileNotFoundError() throws IOException {
        // Given
        String fileName = "valid_audience_status.csv";
        createTestFile(fileName, "");

        when(csvFileProcessor.processCsvFile(any(File.class)))
                .thenThrow(new FileNotFoundException("File not found"));

        // When
        statusScheduler.processAudienceStatusFiles();

        // Then
        verify(csvFileProcessor).processCsvFile(any(File.class));
        verify(service, never()).processAndSaveAudienceStatus(any());
        assertTrue(Files.exists(tempDir.resolve(fileName)), 
            "File should remain in source directory on error");
        assertFalse(Files.exists(processedDir.resolve(fileName)),
            "File should not be moved to processed directory on error");
    }
}
