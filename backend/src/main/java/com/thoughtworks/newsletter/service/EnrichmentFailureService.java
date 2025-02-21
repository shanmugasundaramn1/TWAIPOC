package com.thoughtworks.newsletter.service;

import com.thoughtworks.newsletter.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnrichmentFailureService {
    private final MemberRepository memberRepository;

    public Map<String, Long> getFailureReasonStats(String newsletterName, String partnerName, LocalDate palDate) {
        var failureStats = memberRepository.getFailureReasonStats(newsletterName, partnerName, palDate);
        
        return failureStats.stream()
                .collect(Collectors.toMap(
                        stats -> (String) stats[0],
                        stats -> (Long) stats[1],
                        (existing, replacement) -> existing
                ));
    }
}
