package com.thoughtworks.newsletter.scheduler;

import com.thoughtworks.newsletter.scheduler.dto.SelectedAudienceStatusCsvDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CsvFileProcessorTest {

    private CsvFileProcessor csvFileProcessor;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        csvFileProcessor = new CsvFileProcessor();
    }

    @Test
    void shouldProcessValidCsvFile() throws Exception {
        // Given
        String csvContent = "newsletter_id,date,potential_count,potential_selected_count\n" +
                "1,2024-02-20,100,50";
        File csvFile = createTestFile("valid.csv", csvContent);

        // When
        List<SelectedAudienceStatusCsvDto> result = csvFileProcessor.processCsvFile(csvFile);

        // Then
        assertThat(result).hasSize(1);
        SelectedAudienceStatusCsvDto record = result.get(0);
        assertThat(record.getNewsletterId()).isEqualTo(1L);
        assertThat(record.getDate()).isEqualTo("2024-02-20");
        assertThat(record.getPotentialCount()).isEqualTo("100");
        assertThat(record.getPotentialSelectedCount()).isEqualTo("50");
    }

    @Test
    void shouldHandleInvalidDateFormat() throws Exception {
        // Given
        String csvContent = "newsletter_id,date,potential_count,potential_selected_count\n" +
                "1,invalid-date,100,50";
        File csvFile = createTestFile("invalid_date.csv", csvContent);

        // When/Then
        assertThatThrownBy(() -> csvFileProcessor.processCsvFile(csvFile))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid data format in CSV file");
    }

    @Test
    void shouldHandleInvalidNumericValues() throws Exception {
        // Given
        String csvContent = "newsletter_id,date,potential_count,potential_selected_count\n" +
                "1,2024-02-20,invalid,50";
        File csvFile = createTestFile("invalid_numeric.csv", csvContent);

        // When/Then
        assertThatThrownBy(() -> csvFileProcessor.processCsvFile(csvFile))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid data format in CSV file");
    }

    @Test
    void shouldHandleMissingFields() throws Exception {
        // Given
        String csvContent = "newsletter_id,date,potential_count\n" +
                "1,2024-02-20,100";
        File csvFile = createTestFile("missing_fields.csv", csvContent);

        // When/Then
        assertThatThrownBy(() -> csvFileProcessor.processCsvFile(csvFile))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Missing required fields in CSV file");
    }

    @Test
    void shouldHandleFileNotFound() {
        // Given
        File nonExistentFile = new File(tempDir.toFile(), "non_existent.csv");

        // When/Then
        assertThatThrownBy(() -> csvFileProcessor.processCsvFile(nonExistentFile))
                .isInstanceOf(FileNotFoundException.class)
                .hasMessageContaining("File not found");
    }

    private File createTestFile(String fileName, String content) throws Exception {
        Path filePath = tempDir.resolve(fileName);
        Files.writeString(filePath, content);
        return filePath.toFile();
    }
}
