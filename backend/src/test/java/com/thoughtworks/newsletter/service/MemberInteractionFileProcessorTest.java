package com.thoughtworks.newsletter.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.thoughtworks.newsletter.service.memberinteraction.MemberInteractionFileProcessor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberInteractionFileProcessorTest {
    private MemberInteractionFileProcessor fileProcessor;
    private static final String VENDOR_DIR = "dataSource/vendor";
    private static final String PROCESSED_DIR = "dataSource/vendor/processed";

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        fileProcessor = new MemberInteractionFileProcessor(tempDir.toString());
    }

    @Test
    void shouldListOnlyUnprocessedMemberInteractionFiles() throws IOException {
        // Given
        createDirectories();
        createFile(VENDOR_DIR, "file1.csv");
        createFile(VENDOR_DIR, "file2.csv");
        createFile(VENDOR_DIR, "file3.txt");
        createFile(PROCESSED_DIR, "processed.csv");

        // When
        List<File> files = fileProcessor.listUnprocessedFiles();

        // Then
        assertThat(files).hasSize(2)
                .allMatch(file -> file.getName().endsWith(".csv"))
                .noneMatch(file -> file.getPath().contains(PROCESSED_DIR));
    }

    @Test
    void shouldMarkFileAsProcessedSuccessfully() throws IOException {
        // Given
        createDirectories();
        File sourceFile = createFile(VENDOR_DIR, "test.csv");

        // When
        fileProcessor.markFileAsProcessed(sourceFile);

        // Then
        assertThat(sourceFile).doesNotExist();
        assertThat(new File(tempDir.toString(), PROCESSED_DIR + "/test.csv")).exists();
    }

    @Test
    void shouldThrowExceptionWhenProcessingNonExistentFile() throws IOException {
        // Given
        createDirectories();  // Make sure directories exist
        File nonExistentFile = new File(tempDir.toString(), VENDOR_DIR + "/nonexistent.csv");

        // Then
        assertThatThrownBy(() -> fileProcessor.markFileAsProcessed(nonExistentFile))
                .isInstanceOf(IOException.class)
                .hasMessageContaining("nonexistent.csv");  // Just check for the filename in the error
    }

    private void createDirectories() throws IOException {
        Files.createDirectories(tempDir.resolve(VENDOR_DIR));
        Files.createDirectories(tempDir.resolve(PROCESSED_DIR));
    }

    private File createFile(String directory, String filename) throws IOException {
        Path filePath = tempDir.resolve(directory).resolve(filename);
        Files.createFile(filePath);
        return filePath.toFile();
    }
}
