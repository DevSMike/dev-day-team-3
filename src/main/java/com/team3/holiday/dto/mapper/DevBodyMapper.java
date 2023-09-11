package com.team3.holiday.dto.mapper;

import com.team3.holiday.dto.DevBodyDto;
import com.team3.holiday.dto.DevBodyInfoDto;
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

    public static DevBodyInfoDto toDevBodyInfoDtoFromDevBody(DevBody body) {
        return  DevBodyInfoDto.builder()
                .name(body.getName())
                .nextTaskUrl("http://ya.praktikum.fvds.ru:8080/dev-day/task/2")
                .token("token")
                .build();
    }
}
