package com.thoughtworks.newsletter.domain.service.memberinteraction;

import com.thoughtworks.newsletter.model.MemberInteraction;
import com.thoughtworks.newsletter.repository.MemberInteractionRepository;
import com.thoughtworks.newsletter.domain.service.file.csv.CsvFileProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberInteractionsLoaderServiceTest {
    @Mock
    private MemberInteractionFileProcessor fileProcessor;
    @Mock
    private CsvFileProcessor<MemberInteraction> csvProcessor;
    @Mock
    private MemberInteractionRepository repository;

    private MemberInteractionsLoaderService loaderService;

    @BeforeEach
    void setUp() {
        loaderService = new MemberInteractionsLoaderService(fileProcessor, csvProcessor, repository);
    }

    @Test
    void shouldHaveCorrectSchedulingConfiguration() throws NoSuchMethodException {
        // Given
        Scheduled annotation = MemberInteractionsLoaderService.class
                .getDeclaredMethod("loadMemberInteractions")
                .getAnnotation(Scheduled.class);

        // Then
        assertThat(annotation).isNotNull();
        assertThat(annotation.cron()).isEqualTo("${member-interactions.processing.cron}");
    }

    @Test
    void shouldProcessAllUnprocessedFiles() throws IOException {
        // Given
        File file1 = new File("file1.csv");
        File file2 = new File("file2.csv");
        List<File> unprocessedFiles = List.of(file1, file2);
        
        MemberInteraction interaction = MemberInteraction.builder()
                .memberUuid(UUID.randomUUID())
                .newsletterId(1L)
                .build();
        List<MemberInteraction> interactions = List.of(interaction);

        when(fileProcessor.listUnprocessedFiles()).thenReturn(unprocessedFiles);
        when(csvProcessor.processCsvFile(any())).thenReturn(interactions);

        // When
        loaderService.loadMemberInteractions();

        // Then
        verify(repository, times(2)).saveAll(interactions);
        verify(fileProcessor).markFileAsProcessed(file1);
        verify(fileProcessor).markFileAsProcessed(file2);
    }

    @Test
    void shouldStopProcessingOnError() throws IOException {
        // Given
        File file1 = new File("file1.csv");
        File file2 = new File("file2.csv");
        List<File> unprocessedFiles = List.of(file1, file2);

        when(fileProcessor.listUnprocessedFiles()).thenReturn(unprocessedFiles);
        when(csvProcessor.processCsvFile(file1)).thenThrow(new IOException("Processing error"));

        // When
        loaderService.loadMemberInteractions();

        // Then
        verify(csvProcessor, times(1)).processCsvFile(any());
        verify(repository, never()).saveAll(any());
        verify(fileProcessor, never()).markFileAsProcessed(any());
    }
}
