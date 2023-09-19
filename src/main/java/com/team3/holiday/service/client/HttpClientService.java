package com.team3.holiday.service.client;

import com.team3.holiday.dto.DevBodyDto;
import com.team3.holiday.dto.DevBodyInfo;
import com.team3.holiday.model.DevBody;

public interface HttpClientService {

    DevBodyDto registerUser(DevBody body);

    DevBodyInfo getRegistrationAnswer(DevBody body);

    String decodeMessage(String code);

    String generatePassword();

    String decodeCongratulations(String coded);

    int tryDecodedPass(String bodyValue);
}
