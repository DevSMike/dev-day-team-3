package com.team3.holiday.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ServerAnswerDto {

    private Boolean completed;
    private String message;
    private String nextTaskUrl;
}
