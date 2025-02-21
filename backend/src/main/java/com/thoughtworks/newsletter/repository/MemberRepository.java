package com.thoughtworks.newsletter.repository;

import com.thoughtworks.newsletter.model.EnrichmentStatus;
import com.thoughtworks.newsletter.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    
    @Query("SELECT COUNT(m) FROM Member m WHERE m.newsletterId = :newsletterId AND m.status = :status")
    long countByNewsletterIdAndStatus(Long newsletterId, EnrichmentStatus status);
    
    @Query("SELECT m.status, COUNT(m) FROM Member m WHERE m.newsletterId = :newsletterId GROUP BY m.status")
    List<Object[]> getEnrichmentStatsByNewsletterId(Long newsletterId);
    
    List<Member> findByNewsletterIdOrderByProcessedAtDesc(Long newsletterId);
    
    List<Member> findByStatusAndErrorMessageIsNotNull(EnrichmentStatus status);
}
