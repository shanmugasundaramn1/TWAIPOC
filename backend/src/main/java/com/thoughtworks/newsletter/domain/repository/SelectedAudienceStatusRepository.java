package com.thoughtworks.newsletter.domain.repository;

import com.thoughtworks.newsletter.domain.model.SelectedAudienceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SelectedAudienceStatusRepository extends JpaRepository<SelectedAudienceStatus, Long> {

    Optional<SelectedAudienceStatus> findByNewsletterIdAndDate(Long newsletterId, LocalDate date);

    List<SelectedAudienceStatus> findByDateBetween(LocalDate startDate, LocalDate endDate);

    List<SelectedAudienceStatus> findByNewsletterId(Long newsletterId);
}
