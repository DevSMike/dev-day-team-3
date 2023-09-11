package com.team3.holiday.dto;

import com.team3.holiday.model.Dev;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DevBodyDto {

    private List<Dev> participants;
    private String name;
    private String gitHubUrl;
    private String requestBodyJson;
}
