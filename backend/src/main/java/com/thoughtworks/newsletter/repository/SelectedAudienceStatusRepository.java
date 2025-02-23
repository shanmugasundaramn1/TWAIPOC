package com.thoughtworks.newsletter.repository;

import com.thoughtworks.newsletter.model.SelectedAudienceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;

@Repository
public interface SelectedAudienceStatusRepository extends JpaRepository<SelectedAudienceStatus, Long> {

    Optional<SelectedAudienceStatus> findByNewsletterNewsletterIdAndDate(Long newsletterId, LocalDate date);

    List<SelectedAudienceStatus> findByDateBetween(LocalDate startDate, LocalDate endDate);

    List<SelectedAudienceStatus> findByNewsletterNewsletterId(Long newsletterId);

    @Query("SELECT COUNT(s) FROM SelectedAudienceStatus s " +
           "JOIN s.newsletter n " +
           "WHERE n.newsletterName = COALESCE(:newsletterName, n.newsletterName) " +
           "AND n.palDate = COALESCE(:palDate, n.palDate) " +
           "AND n.partnerName = COALESCE(:partnerName, n.partnerName)")
    Long countByNewsletterFilters(String newsletterName, LocalDate palDate, String partnerName);
}
