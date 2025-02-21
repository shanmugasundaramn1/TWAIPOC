package com.thoughtworks.newsletter.controller;

import com.thoughtworks.newsletter.service.NewsletterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;

@WebMvcTest(NewsletterController.class)
class NewsletterControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NewsletterService service;

    @Test
    void shouldReturnListOfNewsletterNames() throws Exception {
        List<String> newsletters = Arrays.asList("Newsletter1", "Newsletter2");
        when(service.getAllNewsletterNames()).thenReturn(newsletters);

        mockMvc.perform(get("/api/newsletters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]").value("Newsletter1"))
                .andExpect(jsonPath("$[1]").value("Newsletter2"));
    }

    @Test
    void shouldReturnListOfPartnerNames() throws Exception {
        List<String> partners = Arrays.asList("Partner1", "Partner2");
        when(service.getAllPartnerNames()).thenReturn(partners);

        mockMvc.perform(get("/api/partners"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]").value("Partner1"))
                .andExpect(jsonPath("$[1]").value("Partner2"));
    }
}
