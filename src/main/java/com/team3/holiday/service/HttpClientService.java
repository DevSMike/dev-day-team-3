package com.team3.holiday.service;

import com.team3.holiday.dto.DevBodyDto;
import com.team3.holiday.dto.DevBodyInfoDto;
import com.team3.holiday.model.DevBody;

public interface HttpClientService {

    DevBodyDto registerUser(DevBody body);

    DevBodyInfoDto getRegistrationAnswer(DevBody body);

    String decodeMessage(String code);
}
