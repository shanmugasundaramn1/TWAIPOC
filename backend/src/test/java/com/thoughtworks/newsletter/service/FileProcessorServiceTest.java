package com.thoughtworks.newsletter.service;

import com.thoughtworks.newsletter.model.EnrichmentStatus;
import com.thoughtworks.newsletter.model.Member;
import com.thoughtworks.newsletter.repository.MemberRepository;

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
    void shouldProcessValidFileWithErrorMessages() throws IOException {
        // Given
        UUID memberId1 = UUID.randomUUID();
        UUID memberId2 = UUID.randomUUID();
        String content = "newsletter_id,member_id,status,error_message\n" +
                        "1," + memberId1 + ",enriched,\n" +
                        "1," + memberId2 + ",failed,Error occurred";
        Path file = inputDir.resolve("test.csv");
        Files.writeString(file, content);

        when(memberRepository.saveAll(any())).thenAnswer(i -> i.getArguments()[0]);

        // When
        fileProcessorService.processFiles();

        // Then
        verify(memberRepository).saveAll(membersCaptor.capture());
        List<Member> savedMembers = membersCaptor.getValue();
        assertThat(savedMembers).hasSize(2);
        assertThat(savedMembers).hasSize(2);
        
        Member enrichedMember = savedMembers.stream()
            .filter(m -> m.getMemberId().equals(memberId1))
            .findFirst()
            .orElseThrow();
        assertThat(enrichedMember.getStatus()).isEqualTo(EnrichmentStatus.ENRICHED);
        assertThat(enrichedMember.getErrorMessage()).isNull();
        
        Member failedMember = savedMembers.stream()
            .filter(m -> m.getMemberId().equals(memberId2))
            .findFirst()
            .orElseThrow();
        assertThat(failedMember.getStatus()).isEqualTo(EnrichmentStatus.FAILED);
        assertThat(failedMember.getErrorMessage()).isEqualTo("Error occurred");

        assertThat(Files.exists(processedDir.resolve("test.csv"))).isTrue();
        assertThat(Files.exists(file)).isFalse();
    }

    @Test
    void shouldHandleInvalidFile() throws IOException {
        // Given
        String content = "invalid,header,wrong,extra\n1,2,3,4";
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
        String content = "newsletter_id,member_id,status,error_message\n" +
                        "1," + validMemberId1 + ",enriched,\n" +
                        "invalid," + UUID.randomUUID() + ",failed,Some error\n" + // This line will be skipped
                        "1," + validMemberId2 + ",failed,Another error"; // This valid line will be processed
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
