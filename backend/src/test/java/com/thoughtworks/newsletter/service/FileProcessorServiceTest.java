package com.thoughtworks.newsletter.service;

import com.thoughtworks.newsletter.domain.model.EnrichmentStatus;
import com.thoughtworks.newsletter.domain.model.Member;
import com.thoughtworks.newsletter.domain.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileProcessorServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private FileProcessorService fileProcessorService;

    @Captor
    private ArgumentCaptor<List<Member>> membersCaptor;

    @TempDir
    Path tempDir;

    private Path inputDir;
    private Path processedDir;

    @BeforeEach
    void setUp() throws IOException {
        inputDir = tempDir.resolve("input");
        processedDir = tempDir.resolve("processed");
        Files.createDirectories(inputDir);
        Files.createDirectories(processedDir);

        ReflectionTestUtils.setField(fileProcessorService, "inputDirectory", inputDir.toString());
        ReflectionTestUtils.setField(fileProcessorService, "processedDirectory", processedDir.toString());
    }

    @Test
    void shouldProcessValidFile() throws IOException {
        // Given
        String content = "newsletter_id,member_id,status\n" +
                        "1," + UUID.randomUUID() + ",enriched\n" +
                        "1," + UUID.randomUUID() + ",enriched";
        Path file = inputDir.resolve("test.csv");
        Files.writeString(file, content);

        when(memberRepository.saveAll(any())).thenAnswer(i -> i.getArguments()[0]);

        // When
        fileProcessorService.processFiles();

        // Then
        verify(memberRepository).saveAll(membersCaptor.capture());
        List<Member> savedMembers = membersCaptor.getValue();
        assertThat(savedMembers).hasSize(2);
        assertThat(savedMembers).allMatch(m -> 
            m.getNewsletterId().equals(1L) &&
            m.getStatus() == EnrichmentStatus.ENRICHED &&
            m.getFileName().equals("test.csv") &&
            m.getProcessedAt() != null
        );

        assertThat(Files.exists(processedDir.resolve("test.csv"))).isTrue();
        assertThat(Files.exists(file)).isFalse();
    }

    @Test
    void shouldHandleInvalidFile() throws IOException {
        // Given
        String content = "invalid,header,wrong\n1,2,3";
        Path file = inputDir.resolve("invalid.csv");
        Files.writeString(file, content);

        // When
        fileProcessorService.processFiles();

        // Then
        verify(memberRepository, never()).saveAll(any());
        assertThat(Files.exists(file)).isTrue();
        assertThat(Files.exists(processedDir.resolve("invalid.csv"))).isFalse();
    }

    @Test
    void shouldSkipInvalidRecordsAndProcessValidOnes() throws IOException {
        // Given
        UUID validMemberId1 = UUID.randomUUID();
        UUID validMemberId2 = UUID.randomUUID();
        String content = "newsletter_id,member_id,status\n" +
                        "1," + validMemberId1 + ",enriched\n" +
                        "invalid," + UUID.randomUUID() + ",failed\n" + // This line will be skipped
                        "1," + validMemberId2 + ",failed"; // This valid line will be processed
        Path file = inputDir.resolve("mixed-records.csv");
        Files.writeString(file, content);

        when(memberRepository.saveAll(any())).thenAnswer(i -> i.getArguments()[0]);

        // When
        fileProcessorService.processFiles();

        // Then
        verify(memberRepository).saveAll(membersCaptor.capture());
        List<Member> savedMembers = membersCaptor.getValue();
        assertThat(savedMembers).hasSize(2);
        assertThat(savedMembers).extracting(Member::getMemberId)
                               .containsExactlyInAnyOrder(validMemberId1, validMemberId2);
        assertThat(savedMembers).extracting(Member::getStatus)
                               .containsExactlyInAnyOrder(EnrichmentStatus.ENRICHED, EnrichmentStatus.FAILED);
    }
}
