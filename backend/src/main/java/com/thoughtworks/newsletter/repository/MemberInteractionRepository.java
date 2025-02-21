package com.thoughtworks.newsletter.domain.repository;

import com.thoughtworks.newsletter.domain.model.MemberInteraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberInteractionRepository extends JpaRepository<MemberInteraction, Long> {
}
