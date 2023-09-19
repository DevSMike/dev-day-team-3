package com.team3.holiday.service.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team3.holiday.dto.DevBodyDto;
import com.team3.holiday.dto.DevBodyInfo;
import com.team3.holiday.model.DevBody;
import com.team3.holiday.util.DecoderRequesterUtil;
import com.team3.holiday.service.password.PasswordGenerationServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.team3.holiday.dto.mapper.DevBodyMapper.toDevBodyDtoFromDevBody;
import static com.team3.holiday.dto.mapper.DevBodyMapper.toDevBodyInfoDtoFromDevBody;
import static com.team3.holiday.util.DecodeCaesarCipherUtil.decodeCaesarCipher;
import static com.team3.holiday.util.ExtractFromStringUtil.extractValueFromCodeSpan;
import static com.team3.holiday.util.ExtractFromStringUtil.extractValueFromResponse;

@Service
@Slf4j
@RequiredArgsConstructor
public class HttpClientServiceImpl implements HttpClientService {

    private final PasswordGenerationServiceImpl passGenService;

    @Override
    public DevBodyDto registerUser(DevBody body) {
        log.debug("HttpClientServiceImpl: registerUser with object: " + body);
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", body.getName());
        requestBody.put("gitHubUrl", body.getGitHubUrl());
        requestBody.put("participants", body.getParticipants());

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBodyJson;
        try {
            requestBodyJson = objectMapper.writeValueAsString(requestBody);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка при преобразовании в JSON", e);
        }

        return toDevBodyDtoFromDevBody(body, requestBodyJson);
    }

    @Override
    public DevBodyInfo getRegistrationAnswer(DevBody body) {
        log.debug("HttpClientServiceImpl: answer a reg with object: " + body);
        return toDevBodyInfoDtoFromDevBody(body);
    }

    @Override
    public String decodeMessage(String code) {
        String textFromHtml = extractValueFromResponse(code, "encoded");
        String encodedText = textFromHtml.substring(1, textFromHtml.lastIndexOf(",") - 1);
        int offset = Integer.parseInt(textFromHtml.substring(textFromHtml.lastIndexOf(" ") + 2));
        String decodedText = decodeCaesarCipher(encodedText, offset);

        // Создаем JSON для POST-запроса
        return "{\"decoded\": \"" + decodedText + "\"}";
    }

    @Override
    public String generatePassword() {
        log.info("HttpClientService: hacking the pass");
        return passGenService.hackPass();
    }

    @Override
    public String generateLocalPassword() {
        log.info("HttpClientService: hacking the local pass");
        return passGenService.hackLocalPass();
    }


    @Override
    public String decodeCongratulations(String HTMLText) {

        String codedString = extractValueFromCodeSpan(HTMLText);
        String result = "";
        try {
            result = DecoderRequesterUtil.getDecodedString(codedString);

        } catch (Exception e) {
            log.debug("Error while decoding message: " + e.getMessage());
        }
        return result;

    }


}
