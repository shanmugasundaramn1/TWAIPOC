package com.thoughtworks.newsletter.domain.service.memberinteraction;

import com.thoughtworks.newsletter.model.MemberInteraction;
import com.thoughtworks.newsletter.domain.service.file.csv.CsvEntityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MemberInteractionCsvMapper implements CsvEntityMapper<MemberInteraction> {
    private static final Logger logger = LoggerFactory.getLogger(MemberInteractionCsvMapper.class);
    private static final String[] EXPECTED_HEADERS = {
            "member_uuid", "newsletter_id", "delivery_timestamp",
            "open_timestamp", "coupon_click_timestamp"
    };

    @Override
    public String[] getExpectedHeaders() {
        return EXPECTED_HEADERS;
    }

    @Override
    public MemberInteraction mapFromCsv(String[] values) {
        try {
            if (values.length != EXPECTED_HEADERS.length) {
                logger.warn("Invalid row format: Wrong number of columns");
                return null;
            }

            return MemberInteraction.builder()
                    .memberUuid(UUID.fromString(values[0].trim()))
                    .newsletterId(Long.parseLong(values[1].trim()))
                    .deliveryTimestamp(parseNullableLong(values[2]))
                    .openTimestamp(parseNullableLong(values[3]))
                    .couponClickTimestamp(parseNullableLong(values[4]))
                    .build();
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid data format in row: {}", String.join(",", values), e);
            return null;
        }
    }

    private Long parseNullableLong(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
