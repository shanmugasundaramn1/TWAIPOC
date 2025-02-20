package com.thoughtworks.newsletter.scheduler.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SelectedAudienceStatusCsvDto {

    @CsvBindByName(column = "newsletter_id")
    private Long newsletterId;

    @CsvBindByName(column = "date")
    private String date;

    @CsvBindByName(column = "potential_count")
    private String potentialCount;

    @CsvBindByName(column = "potential_selected_count")
    private String potentialSelectedCount;
}
