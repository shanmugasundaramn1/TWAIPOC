package com.thoughtworks.newsletter.domain.repository;

import com.thoughtworks.newsletter.domain.model.Newsletter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsletterRepository extends JpaRepository<Newsletter, Long> {
    Newsletter findByNewsletterId(Long newsletterId);
}
