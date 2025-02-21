package com.thoughtworks.newsletter.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SchedulerServiceTest {

    @Mock
    private FileProcessorService fileProcessorService;

    @InjectMocks
    private SchedulerService schedulerService;

    @Test
    void shouldCallFileProcessorOnSchedule() {
        // When
        schedulerService.processFiles();

        // Then
        verify(fileProcessorService).processFiles();
    }
}
