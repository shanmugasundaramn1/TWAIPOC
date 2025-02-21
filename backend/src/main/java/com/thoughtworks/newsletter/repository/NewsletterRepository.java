package com.thoughtworks.newsletter.repository;

import com.thoughtworks.newsletter.model.Newsletter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

@Repository
public interface NewsletterRepository extends JpaRepository<Newsletter, Long> {
    Newsletter findByNewsletterId(Long newsletterId);
    
    @Query("SELECT DISTINCT n.newsletterName FROM Newsletter n")
    List<String> findDistinctNewsletterName();

    @Query("SELECT DISTINCT n.partnerName FROM Newsletter n")
    List<String> findDistinctPartnerName();
}
