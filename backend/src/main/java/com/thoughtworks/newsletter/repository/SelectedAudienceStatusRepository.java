package com.thoughtworks.newsletter.repository;

import com.thoughtworks.newsletter.model.SelectedAudienceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;

@Repository
public interface SelectedAudienceStatusRepository extends JpaRepository<SelectedAudienceStatus, Long> {

    Optional<SelectedAudienceStatus> findByNewsletterNewsletterIdAndDate(Long newsletterId, LocalDate date);

    List<SelectedAudienceStatus> findByDateBetween(LocalDate startDate, LocalDate endDate);

    List<SelectedAudienceStatus> findByNewsletterNewsletterId(Long newsletterId);

    @Query("SELECT COUNT(s) FROM SelectedAudienceStatus s " +
           "JOIN s.newsletter n " +
           "WHERE (:newsletterName IS NULL OR n.newsletterName = :newsletterName) " +
           "AND (:date IS NULL OR s.date = :date) " +
           "AND (:partnerName IS NULL OR n.partnerName = :partnerName)")
    Long countByNewsletterFilters(@Param("newsletterName") String newsletterName, 
                                @Param("date") LocalDate date, 
                                @Param("partnerName") String partnerName);
} 