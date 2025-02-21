package com.thoughtworks.newsletter.domain.service.file.csv;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

@Component
public class CsvFileProcessor<T> {
    private static final Logger logger = LoggerFactory.getLogger(CsvFileProcessor.class);
    private final CsvEntityMapper<T> entityMapper;

    public CsvFileProcessor(CsvEntityMapper<T> entityMapper) {
        this.entityMapper = entityMapper;
    }

    public List<T> processCsvFile(File file) throws IOException {
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(file))
                .withSkipLines(0)
                .build()) {

            List<String[]> rows = reader.readAll();
            if (rows.isEmpty()) {
                throw new IOException("Invalid CSV format: File is empty");
            }

            validateHeaders(rows.get(0), entityMapper.getExpectedHeaders());
            return rows.stream()
                    .skip(1) // Skip header row
                    .filter(row -> row.length > 0 && row[0].trim().length() > 0)
                    .map(entityMapper::mapFromCsv)
                    .filter(Objects::nonNull)
                    .toList();

        } catch (CsvException e) {
            logger.error("Error reading CSV file: {}", file.getName(), e);
            throw new IOException("Failed to read CSV file", e);
        }
    }

    private void validateHeaders(String[] headers, String[] expectedHeaders) throws IOException {
        if (!Arrays.equals(headers, expectedHeaders)) {
            throw new IOException("Invalid CSV format: Unexpected headers");
        }
    }
}
