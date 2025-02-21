package com.thoughtworks.newsletter.repository;

import com.thoughtworks.newsletter.model.EnrichmentStatus;
import com.thoughtworks.newsletter.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import com.thoughtworks.newsletter.dto.InteractionCountsDto;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    
    @Query("SELECT COUNT(m) FROM Member m WHERE m.newsletterId = :newsletterId AND m.status = :status")
    long countByNewsletterIdAndStatus(Long newsletterId, EnrichmentStatus status);
    
    @Query("SELECT m.status, COUNT(m) FROM Member m WHERE m.newsletterId = :newsletterId GROUP BY m.status")
    List<Object[]> getEnrichmentStatsByNewsletterId(Long newsletterId);
    
    List<Member> findByNewsletterIdOrderByProcessedAtDesc(Long newsletterId);
    
    List<Member> findByStatusAndErrorMessageIsNotNull(EnrichmentStatus status);

    @Query("SELECT count(m.id) " +
           "FROM Newsletter n " +
           "INNER JOIN Member m ON m.newsletterId = n.id " +
           "WHERE n.newsletterName = COALESCE(:newsletterName, n.newsletterName) " +
           "AND n.palDate = COALESCE(:palDate, n.palDate) " +
           "AND n.partnerName = COALESCE(:partnerName, n.partnerName)")
    Long getEnrichedCount(String newsletterName, LocalDate palDate, String partnerName);

    @Query("SELECT m.errorMessage, COUNT(m) " +
           "FROM Member m " +
           "JOIN Newsletter n ON m.newsletterId = n.newsletterId " +
           "WHERE m.status = 'FAILED' " +
           "AND n.newsletterName = :newsletterName " +
           "AND n.partnerName = :partnerName " +
           "AND n.palDate = :palDate " +
           "GROUP BY m.errorMessage")
    List<Object[]> getFailureReasonStats(
            @Param("newsletterName") String newsletterName,
            @Param("partnerName") String partnerName,
            @Param("palDate") LocalDate palDate);
}
