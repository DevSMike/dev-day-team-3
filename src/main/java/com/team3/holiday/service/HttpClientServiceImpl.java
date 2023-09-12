package com.team3.holiday.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;
import com.team3.holiday.dto.DevBodyDto;
import com.team3.holiday.dto.DevBodyInfoDto;
import com.team3.holiday.model.DevBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.io.UnsupportedEncodingException;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static com.team3.holiday.dto.mapper.DevBodyMapper.toDevBodyDtoFromDevBody;
import static com.team3.holiday.dto.mapper.DevBodyMapper.toDevBodyInfoDtoFromDevBody;
import static com.team3.holiday.util.DecodeCaesarCipherUtil.decodeCaesarCipher;
import static com.team3.holiday.util.DecodeCharsetUtils.detectCharset;
import static com.team3.holiday.util.ExtractFromStringUtil.extractValueFromResponse;
import static com.team3.holiday.util.PasswordGeneratorUtil.generatePasswords;

@Service
@Slf4j
public class HttpClientServiceImpl implements HttpClientService {

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
    public DevBodyInfoDto getRegistrationAnswer(DevBody body) {
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
        String characters = "0123456789ABCDEFabcdef";
        int passwordLength = 8; // Максимальная длина пароля
        return generatePasswords(characters, passwordLength);
    }

    @Override
    public String decodeCongratulations(String coded)   {
//        Charset charset = detectCharset(coded.getBytes());
//
//        if (charset != null) {
//            String result = new String(coded.getBytes(charset), StandardCharsets.UTF_8);
//            return result;
//        } else {
//            System.out.println("Не удалось определить кодировку.");
//        }
//        return "";
        // Перевод строки в байты с использованием неправильной кодировки (UTF-8)
       String result = null;

        for (Charset charset : Charset.availableCharsets().values()) {
            try {
                byte[] bytesInCharset = coded.getBytes(charset);
                String decodedText = new String(bytesInCharset, charset);

                boolean containsOnlyRussianLetters = decodedText.matches("^[а-яА-Я]*$");

                if (containsOnlyRussianLetters) {
                    result = decodedText;
                    break;
                }
            } catch (Exception e) {
                continue;
            }


        }
        return  result;
        }

//        Charset[] charsets = {
//                StandardCharsets.UTF_8,
//                StandardCharsets.ISO_8859_1,
//                Charset.forName("Windows-1251"),
//                Charset.forName("KOI8-R")
//        };
//
//        for (Charset charset : charsets) {
//            String decodedString = decodeString(coded, charset);
//            System.out.println("Decoded using " + charset.displayName() + ": " + decodedString);
//        }
//        return "";

//        Charset sourceCharset = Charset.forName("windows-1251"); // Исходная кодировка (Windows-1251)
//        Charset targetCharset = StandardCharsets.UTF_8; // Целевая кодировка (UTF-8)
//
//        try {
//            byte[] inputBytes = coded.getBytes(sourceCharset);
//            String decodedString = new String(inputBytes, targetCharset);
//            System.out.println(decodedString);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return "";


       // String windows1251String = "Øèðîêàÿ ýëåêòðèôèêàöèÿ";
//        String utf8String = new String(coded.getBytes("Windows-1251"), "UTF-8");
//        System.out.println(utf8String);
//        return "";




    private String decodeString(String input, Charset charset) {
        CharsetDecoder decoder = charset.newDecoder();
        ByteBuffer inputBuffer = ByteBuffer.wrap(input.getBytes());
        CharBuffer outputBuffer;

        try {
            outputBuffer = decoder.decode(inputBuffer);
        } catch (Exception e) {
            e.printStackTrace();
            return "Decoding failed";
        }

        return outputBuffer.toString();
    }
}
