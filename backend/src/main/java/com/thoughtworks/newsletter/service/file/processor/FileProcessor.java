package com.thoughtworks.newsletter.service.file.processor;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface FileProcessor {
    /**
     * List all unprocessed files in the source directory
     * @return List of unprocessed files
     * @throws IOException if there's an error accessing the directory
     */
    List<File> listUnprocessedFiles() throws IOException;

    /**
     * Mark a file as processed by moving it to the processed directory
     * @param file The file to mark as processed
     * @throws IOException if there's an error moving the file
     */
    void markFileAsProcessed(File file) throws IOException;
}
