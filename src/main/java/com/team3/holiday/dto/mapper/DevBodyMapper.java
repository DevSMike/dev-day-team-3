package com.team3.holiday.dto.mapper;

import com.team3.holiday.dto.DevBodyDto;
import com.team3.holiday.dto.DevBodyInfo;
import com.team3.holiday.model.DevBody;

public class DevBodyMapper {

    public static DevBodyDto toDevBodyDtoFromDevBody(DevBody body, String json) {
        return DevBodyDto.builder()
                .requestBodyJson(json)
                .participants(body.getParticipants())
                .name(body.getName())
                .gitHubUrl(body.getGitHubUrl())
                .build();
    }

    public static DevBodyInfo toDevBodyInfoDtoFromDevBody(DevBody body) {
        return  DevBodyInfo.builder()
                .name(body.getName())
                .nextTaskUrl("http://ya.praktikum.fvds.ru:8080/dev-day/task/2")
                .token("e26d3434-c970-482a-b055-e2a55a364581")
                .build();
    }
}
