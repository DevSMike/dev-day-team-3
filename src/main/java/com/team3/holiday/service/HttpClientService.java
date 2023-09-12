package com.team3.holiday.service;

import com.team3.holiday.dto.DevBodyDto;
import com.team3.holiday.dto.DevBodyInfoDto;
import com.team3.holiday.model.DevBody;

import java.io.UnsupportedEncodingException;

public interface HttpClientService {

    DevBodyDto registerUser(DevBody body);

    DevBodyInfoDto getRegistrationAnswer(DevBody body);

    String decodeMessage(String code);

    String generatePassword();

    String decodeCongratulations(String coded);
}
