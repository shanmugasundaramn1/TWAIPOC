package com.thoughtworks.newsletter.domain.service.file.csv;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CsvFileProcessorTest {
    @Mock
    private CsvEntityMapper<TestEntity> entityMapper;
    private CsvFileProcessor<TestEntity> csvFileProcessor;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        csvFileProcessor = new CsvFileProcessor<>(entityMapper);
    }

    @Test
    void shouldParseValidCsvFile() throws IOException {
        // Given
        String[] headers = {"id", "name"};
        when(entityMapper.getExpectedHeaders()).thenReturn(headers);
        
        String csvContent = """
            id,name
            1,test1
            2,test2
            """;
        File csvFile = createTestFile(csvContent);

        TestEntity entity1 = new TestEntity(1L, "test1");
        TestEntity entity2 = new TestEntity(2L, "test2");
        when(entityMapper.mapFromCsv(new String[]{"1", "test1"})).thenReturn(entity1);
        when(entityMapper.mapFromCsv(new String[]{"2", "test2"})).thenReturn(entity2);

        // When
        List<TestEntity> entities = csvFileProcessor.processCsvFile(csvFile);

        // Then
        assertThat(entities).hasSize(2);
        assertThat(entities).containsExactly(entity1, entity2);
    }

    @Test
    void shouldThrowExceptionForInvalidCsvFormat() throws IOException {
        // Given
        String[] headers = {"id", "name"};
        when(entityMapper.getExpectedHeaders()).thenReturn(headers);
        
        String csvContent = "invalid,csv,format";
        File csvFile = createTestFile(csvContent);

        // Then
        assertThatThrownBy(() -> csvFileProcessor.processCsvFile(csvFile))
                .isInstanceOf(IOException.class)
                .hasMessageContaining("Invalid CSV format");
    }

    private File createTestFile(String content) throws IOException {
        Path filePath = tempDir.resolve("test.csv");
        Files.writeString(filePath, content);
        return filePath.toFile();
    }

    private static class TestEntity {
        private final Long id;
        private final String name;

        TestEntity(Long id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}
