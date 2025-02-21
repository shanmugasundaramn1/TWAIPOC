package com.thoughtworks.newsletter.service.memberinteraction;

import com.thoughtworks.newsletter.service.file.processor.FileProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Stream;

@Component
public class MemberInteractionFileProcessor implements FileProcessor {
    private final String basePath;
    private static final String VENDOR_DIR = "dataSource/vendor";
    private static final String PROCESSED_DIR = "dataSource/vendor/processed";

    public MemberInteractionFileProcessor(@Value("${file.base-path}") String basePath) {
        this.basePath = basePath;
    }

    @Override
    public List<File> listUnprocessedFiles() throws IOException {
        Path vendorPath = Path.of(basePath, VENDOR_DIR);
        try (Stream<Path> paths = Files.walk(vendorPath, 1)) {
            return paths
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .filter(file -> file.getName().endsWith(".csv"))
                    .toList();
        }
    }

    public void markFileAsProcessed(File sourceFile) throws IOException {
        if (!sourceFile.exists()) {
            throw new IOException("Failed to process file: File does not exist - " + sourceFile.getName());
        }

        Path targetPath = Path.of(basePath, PROCESSED_DIR);
        Files.createDirectories(targetPath);
        
        try {
            Files.move(sourceFile.toPath(), targetPath.resolve(sourceFile.getName()), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IOException("Failed to mark member_interactions file as processed: " + sourceFile.getName(), e);
        }
    }
}
