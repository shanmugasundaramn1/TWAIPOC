package com.thoughtworks.newsletter.repository;

import com.thoughtworks.newsletter.model.MemberInteraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;
import com.thoughtworks.newsletter.dto.InteractionCountsDto;

@Repository
public interface MemberInteractionRepository extends JpaRepository<MemberInteraction, Long> {

    @Query("SELECT new com.thoughtworks.newsletter.dto.InteractionCountsDto(" +
           "COUNT(DISTINCT CASE WHEN mi.deliveryTimestamp IS NOT NULL THEN mi.id ELSE null END), " +
           "COUNT(DISTINCT CASE WHEN mi.openTimestamp IS NOT NULL THEN mi.id ELSE null END), " +
           "COUNT(DISTINCT CASE WHEN mi.couponClickTimestamp IS NOT NULL THEN mi.id ELSE null END), " +
           "COUNT(DISTINCT mi.id) - COUNT(DISTINCT CASE WHEN mi.deliveryTimestamp IS NOT NULL THEN mi.id ELSE null END)) " +
           "FROM Newsletter n " +
           "INNER JOIN MemberInteraction mi ON mi.newsletterId = n.id " +
           "WHERE n.newsletterName = COALESCE(:newsletterName, n.newsletterName) " +
           "AND n.palDate = COALESCE(:palDate, n.palDate) " +
           "AND n.partnerName = COALESCE(:partnerName, n.partnerName)")
    InteractionCountsDto getInteractionCounts(String newsletterName, LocalDate palDate, String partnerName);
}
