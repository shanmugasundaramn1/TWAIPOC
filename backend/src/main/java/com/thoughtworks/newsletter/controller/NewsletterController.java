package com.thoughtworks.newsletter.controller;

import com.thoughtworks.newsletter.service.NewsletterService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api")
public class NewsletterController {
    private final NewsletterService service;

    public NewsletterController(NewsletterService service) {
        this.service = service;
    }

    @GetMapping("/newsletters")
    public List<String> getNewsletters() {
        return service.getAllNewsletterNames();
    }

    @GetMapping("/partners")
    public List<String> getPartners() {
        return service.getAllPartnerNames();
    }
}
