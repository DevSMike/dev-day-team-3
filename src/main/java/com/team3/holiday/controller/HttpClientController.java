package com.team3.holiday.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team3.holiday.model.Dev;
import com.team3.holiday.model.DevBody;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@RestController
@RequestMapping("/register")
public class HttpClientController {

    private final WebClient client = WebClient.create();

    @PostMapping()
    public DevBody makeRegister() {
        List<Dev> participants = new ArrayList<>();
        participants.add(Dev.builder().email("starrose777@yandex.ru").cohort("java_33").firstName("Alexander").lastName("Kosarev").build());
        participants.add(Dev.builder().email("dvn96@yandex.ru").cohort("java_29").firstName("Dmitry").lastName("Novik").build());
        participants.add(Dev.builder().email("likashev.migha@yandex.ru").cohort("java_16").firstName("Mikhail").lastName("Lukashev").build());

        // Создайте JSON-объект
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "Team3");
        requestBody.put("gitHubUrl", "https://github.com/DevSMike/dev-day-team-3");
        requestBody.put("participants", participants);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBodyJson;
        try {
            requestBodyJson = objectMapper.writeValueAsString(requestBody);
        } catch (JsonProcessingException e) {
            // Обработайте исключение, если есть ошибка при преобразовании в JSON
            throw new RuntimeException("Ошибка при преобразовании в JSON", e);
        }

        return client.post()
                .uri("http://localhost:8080/register/post")
                .header("MAIN_ANSWER", "42")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(requestBodyJson) // Отправить JSON-строку напрямую
                .retrieve()
                .bodyToMono(DevBody.class).block();
    }


    @PostMapping("/post")
    public void testRegister(@RequestBody DevBody body) {
        System.out.println(body);
    }
}

