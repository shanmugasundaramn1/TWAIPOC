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

import static org.junit.jupiter.api.Assertions.assertTrue;
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
    private CsvFileProcessor csvFileProcessor;

    @Mock
    private SelectedAudienceStatusService service;

    private SelectedAudienceStatusScheduler statusScheduler;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        when(properties.getScheduler()).thenReturn(scheduler);
        when(scheduler.getCsv()).thenReturn(csv);
        statusScheduler = new SelectedAudienceStatusScheduler(properties, csvFileProcessor, service);
    }

    private Path createTestFile(String fileName, String content) throws IOException {
        Path targetPath = tempDir.resolve(fileName);
        Files.writeString(targetPath, content);
        return targetPath;
    }

    @Test
    void shouldProcessValidCsvFiles() throws IOException {
        // Given
        when(csv.getPath()).thenReturn(tempDir.toString());
        
        createTestFile("valid_audience_status.csv", "content");
        List<SelectedAudienceStatusCsvDto> mockRecords = List.of(new SelectedAudienceStatusCsvDto());
        when(csvFileProcessor.processCsvFile(any(File.class))).thenReturn(mockRecords);

        // When
        statusScheduler.processAudienceStatusFiles();

        // Then
        verify(csvFileProcessor).processCsvFile(any(File.class));
        verify(service).processAndSaveAudienceStatus(mockRecords);
    }

    @Test
    void shouldHandleInvalidDirectory() throws FileNotFoundException {
        // Given
        when(csv.getPath()).thenReturn("/invalid/path");

        // When
        statusScheduler.processAudienceStatusFiles();

        // Then
        verify(csvFileProcessor, never()).processCsvFile(any());
        verify(service, never()).processAndSaveAudienceStatus(any());
    }

    @Test
    void shouldHandleEmptyDirectory() throws FileNotFoundException {
        // Given
        when(csv.getPath()).thenReturn(tempDir.toString());

        // When
        statusScheduler.processAudienceStatusFiles();

        // Then
        verify(csvFileProcessor, never()).processCsvFile(any());
        verify(service, never()).processAndSaveAudienceStatus(any());
    }

    @Test
    void shouldHandleProcessingError() throws IOException {
        // Given
        when(csv.getPath()).thenReturn(tempDir.toString());
        createTestFile("error.csv", "content");
        when(csvFileProcessor.processCsvFile(any(File.class)))
            .thenThrow(new IllegalArgumentException("Processing error"));

        // When
        statusScheduler.processAudienceStatusFiles();

        // Then
        verify(csvFileProcessor).processCsvFile(any(File.class));
        verify(service, never()).processAndSaveAudienceStatus(any());
    }

    @Test
    void shouldProcessMultipleFiles() throws IOException {
        // Given
        when(csv.getPath()).thenReturn(tempDir.toString());

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
    }

    @Test
    @SneakyThrows
    void shouldHandleFileNotFoundError() throws FileNotFoundException, IOException {
        // Given
        String csvPath = tempDir.toString();
        doReturn(csvPath).when(csv).getPath();

        // Create test CSV file path (but don't create the file)
        createTestFile("valid_audience_status.csv", "");

        doThrow(new FileNotFoundException("File not found"))
                .when(csvFileProcessor).processCsvFile(any(File.class));

        // When
        statusScheduler.processAudienceStatusFiles();

        // Then
        verify(csvFileProcessor).processCsvFile(any(File.class));
        verify(service, never()).processAndSaveAudienceStatus(any());
    }
}
