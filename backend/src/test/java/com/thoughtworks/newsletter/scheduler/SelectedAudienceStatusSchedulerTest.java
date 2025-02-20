package com.thoughtworks.newsletter.scheduler;

import com.thoughtworks.newsletter.config.NewsletterProperties;
import com.thoughtworks.newsletter.scheduler.dto.SelectedAudienceStatusCsvDto;
import com.thoughtworks.newsletter.service.SelectedAudienceStatusService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    @SneakyThrows
    void setUp() {
        doReturn(scheduler).when(properties).getScheduler();
        doReturn(csv).when(scheduler).getCsv();
        doReturn(List.of()).when(csvFileProcessor).processCsvFile(any(File.class));
        statusScheduler = new SelectedAudienceStatusScheduler(properties, csvFileProcessor, service);
    }

    @SneakyThrows
    private Path createTestFile(String fileName, String content) {
        Path targetPath = tempDir.resolve(fileName);
        Files.writeString(targetPath, content);
        return targetPath;
    }

    @Test
    @SneakyThrows
    void shouldProcessValidCsvFiles() {
        // Given
        String csvPath = tempDir.toString();
        doReturn(csvPath).when(csv).getPath();

        // Create test CSV file
        String csvContent = "newsletter_id,date,potential_count,potential_selected_count\n" +
                "1,2024-02-20,100,50";
        createTestFile("valid_audience_status.csv", csvContent);

        List<SelectedAudienceStatusCsvDto> mockRecords = List.of(new SelectedAudienceStatusCsvDto());
        doReturn(mockRecords).when(csvFileProcessor).processCsvFile(any(File.class));

        // When
        statusScheduler.processAudienceStatusFiles();

        // Then
        verify(csvFileProcessor).processCsvFile(any(File.class));
        verify(service).processAndSaveAudienceStatus(mockRecords);
    }

    @Test
    @SneakyThrows
    void shouldHandleInvalidDirectory() {
        // Given
        String invalidPath = "/invalid/path";
        doReturn(invalidPath).when(csv).getPath();

        // When
        statusScheduler.processAudienceStatusFiles();

        // Then
        verify(csvFileProcessor, never()).processCsvFile(any());
        verify(service, never()).processAndSaveAudienceStatus(any());
    }

    @Test
    @SneakyThrows
    void shouldHandleEmptyDirectory() {
        // Given
        String emptyDirPath = tempDir.toString();
        doReturn(emptyDirPath).when(csv).getPath();

        // When
        statusScheduler.processAudienceStatusFiles();

        // Then
        verify(csvFileProcessor, never()).processCsvFile(any());
        verify(service, never()).processAndSaveAudienceStatus(any());
    }

    @Test
    @SneakyThrows
    void shouldHandleProcessingError() {
        // Given
        String csvPath = tempDir.toString();
        doReturn(csvPath).when(csv).getPath();

        // Create test CSV file
        String csvContent = "newsletter_id,date,potential_count,potential_selected_count\n" +
                "1,2024-02-20,100,50";
        createTestFile("valid_audience_status.csv", csvContent);

        doThrow(new IllegalArgumentException("Processing error"))
                .when(csvFileProcessor).processCsvFile(any(File.class));

        // When
        statusScheduler.processAudienceStatusFiles();

        // Then
        verify(csvFileProcessor).processCsvFile(any(File.class));
        verify(service, never()).processAndSaveAudienceStatus(any());
    }

    @Test
    @SneakyThrows
    void shouldHandleFileNotFoundError() {
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
