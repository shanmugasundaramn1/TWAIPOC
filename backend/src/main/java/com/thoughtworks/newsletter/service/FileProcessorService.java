package com.thoughtworks.newsletter.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.thoughtworks.newsletter.model.EnrichmentStatus;
import com.thoughtworks.newsletter.model.Member;
import com.thoughtworks.newsletter.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@Slf4j
public class FileProcessorService {

    private final MemberRepository memberRepository;
    private final String inputDirectory;
    private final String processedDirectory;

    public FileProcessorService(
            MemberRepository memberRepository,
            @Value("${app.file-processor.input-directory}") String inputDirectory,
            @Value("${app.file-processor.processed-directory}") String processedDirectory) {
        this.memberRepository = memberRepository;
        this.inputDirectory = inputDirectory;
        this.processedDirectory = processedDirectory;
    }

    @Transactional
    public void processFiles() {
        try (Stream<Path> files = Files.list(Path.of(inputDirectory))) {
            files.filter(path -> path.toString().endsWith(".csv"))
                 .forEach(this::processFile);
        } catch (IOException e) {
                log.error("Error listing files in directory: {}", inputDirectory, e);
        }
    }

    private void processFile(Path file) {
        log.info("Processing file: {}", file.getFileName());
        List<Member> members = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(file.toFile()))) {
            String[] header = reader.readNext();
            if (!isValidHeader(header)) {
                log.error("Invalid header in file: {}", file.getFileName());
                return;
            }

            String[] line;
            while ((line = reader.readNext()) != null) {
                try {
                    members.add(createMember(line, file.getFileName().toString()));
                } catch (Exception e) {
                    log.error("Error processing line in file {}: {}", file.getFileName(), e.getMessage());
                }
            }

            if (!members.isEmpty()) {
                memberRepository.saveAll(members);
            }
            moveToProcessed(file);
        } catch (IOException | CsvValidationException e) {
            log.error("Error reading file: {}", file.getFileName(), e);
        }
    }

    private boolean isValidHeader(String[] header) {
        return header != null && 
               header.length == 4 && 
               "newsletter_id".equals(header[0]) && 
               "member_id".equals(header[1]) &&
               "status".equals(header[2]) &&
               "error_message".equals(header[3]);
    }

    private Member createMember(String[] line, String fileName) {
        if (line.length != 4) {
            throw new IllegalArgumentException("Invalid number of columns");
        }

        Long newsletterId = Long.parseLong(line[0]);
        UUID memberId = UUID.fromString(line[1]);
        EnrichmentStatus status = EnrichmentStatus.valueOf(line[2].toUpperCase());
        String errorMessage = line[3].trim().isEmpty() ? null : line[3];

        return Member.builder()
                .newsletterId(newsletterId)
                .memberId(memberId)
                .status(status)
                .errorMessage(errorMessage)
                .fileName(fileName)
                .processedAt(LocalDateTime.now())
                .build();
    }

    private void moveToProcessed(Path file) throws IOException {
        Path processedPath = Path.of(processedDirectory, file.getFileName().toString());
        Files.move(file, processedPath, StandardCopyOption.REPLACE_EXISTING);
        log.info("Moved file to processed directory: {}", processedPath);
    }
}
