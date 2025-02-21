package com.thoughtworks.newsletter.domain.service.file.csv;

public interface CsvEntityMapper<T> {
    /**
     * Get the expected CSV headers for this entity
     * @return Array of expected header strings
     */
    String[] getExpectedHeaders();

    /**
     * Map a CSV row to an entity
     * @param values Array of string values from CSV row
     * @return Mapped entity or null if mapping fails
     */
    T mapFromCsv(String[] values);
}
