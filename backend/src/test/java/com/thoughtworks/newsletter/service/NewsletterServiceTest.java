package com.thoughtworks.newsletter.service;

import com.thoughtworks.newsletter.model.Newsletter;
import com.thoughtworks.newsletter.repository.NewsletterRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NewsletterServiceTest {
    @Mock
    private NewsletterRepository repository;

    @InjectMocks
    private NewsletterService service;

    @Test
    void shouldReturnExistingNewsletterWhenFound() {
        Long newsletterId = 1L;
        Newsletter existingNewsletter = Newsletter.builder()
                .newsletterId(newsletterId)
                .newsletterName("Existing Newsletter")
                .partnerName("Existing Partner")
                .build();
        
        when(repository.findByNewsletterId(newsletterId)).thenReturn(existingNewsletter);

        Newsletter result = service.getOrCreateNewsletter(newsletterId);

        assertEquals(existingNewsletter, result);
        verify(repository).findByNewsletterId(newsletterId);
    }

    @Test
    void shouldCreateNewNewsletterWhenNotFound() {
        Long newsletterId = 1L;
        when(repository.findByNewsletterId(newsletterId)).thenReturn(null);
        
        Newsletter savedNewsletter = Newsletter.builder()
                .newsletterId(newsletterId)
                .newsletterName("Newsletter " + newsletterId)
                .partnerName("Partner " + newsletterId)
                .build();
        when(repository.save(any(Newsletter.class))).thenReturn(savedNewsletter);

        Newsletter result = service.getOrCreateNewsletter(newsletterId);

        assertNotNull(result);
        assertEquals(newsletterId, result.getNewsletterId());
        assertEquals("Newsletter " + newsletterId, result.getNewsletterName());
        assertEquals("Partner " + newsletterId, result.getPartnerName());

        ArgumentCaptor<Newsletter> newsletterCaptor = ArgumentCaptor.forClass(Newsletter.class);
        verify(repository).save(newsletterCaptor.capture());
        Newsletter capturedNewsletter = newsletterCaptor.getValue();
        
        assertEquals(newsletterId, capturedNewsletter.getNewsletterId());
        assertNotNull(capturedNewsletter.getPalDate());
        assertNotNull(capturedNewsletter.getCreatedAt());
        assertNotNull(capturedNewsletter.getUpdatedAt());
    }

    @Test
    void shouldReturnAllDistinctNewsletterNames() {
        List<String> expectedNewsletters = Arrays.asList("Newsletter1", "Newsletter2");
        when(repository.findDistinctNewsletterName()).thenReturn(expectedNewsletters);

        List<String> actualNewsletters = service.getAllNewsletterNames();

        assertEquals(expectedNewsletters, actualNewsletters);
        verify(repository).findDistinctNewsletterName();
    }

    @Test
    void shouldReturnAllDistinctPartnerNames() {
        List<String> expectedPartners = Arrays.asList("Partner1", "Partner2");
        when(repository.findDistinctPartnerName()).thenReturn(expectedPartners);

        List<String> actualPartners = service.getAllPartnerNames();

        assertEquals(expectedPartners, actualPartners);
        verify(repository).findDistinctPartnerName();
    }
}
