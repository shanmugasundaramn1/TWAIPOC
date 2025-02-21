package com.thoughtworks.newsletter.scheduler;

import com.opencsv.bean.CsvToBeanBuilder;
import com.thoughtworks.newsletter.scheduler.dto.SelectedAudienceStatusCsvDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
public class SelectedAudienceStatusCsvFileProcessor {

    public List<SelectedAudienceStatusCsvDto> processCsvFile(File file) throws FileNotFoundException {
        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + file.getAbsolutePath());
        }

        try (FileReader reader = new FileReader(file)) {
            List<SelectedAudienceStatusCsvDto> csvRecords = new CsvToBeanBuilder<SelectedAudienceStatusCsvDto>(reader)
                    .withType(SelectedAudienceStatusCsvDto.class)
                    .build()
                    .parse();

            validateCsvRecords(csvRecords);
            return csvRecords;
        } catch (Exception e) {
            log.error("Error processing CSV file {}: {}", file.getName(), e.getMessage());
            throw new IllegalArgumentException("Invalid data format in CSV file: " + e.getMessage(), e);
        }
    }

    private void validateCsvRecords(List<SelectedAudienceStatusCsvDto> records) {
        if (records == null) {
            throw new IllegalArgumentException("Invalid CSV file format: records are null");
        }

        for (SelectedAudienceStatusCsvDto record : records) {
            if (record.getNewsletterId() == null ||
                    record.getDate() == null ||
                    record.getPotentialCount() == null ||
                    record.getPotentialSelectedCount() == null) {
                throw new IllegalArgumentException("Missing required fields in CSV file");
            }

            try {
                // Validate date format
                LocalDate.parse(record.getDate());

                // Validate numeric fields
                Integer.parseInt(record.getPotentialCount());
                Integer.parseInt(record.getPotentialSelectedCount());
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid data format in CSV file: " + e.getMessage());
            }
        }
    }
}
