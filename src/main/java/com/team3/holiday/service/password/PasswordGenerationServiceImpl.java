package com.team3.holiday.service.password;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;


@Slf4j
public class PasswordGenerationServiceImpl implements PasswordGenerationService {

    private final WebClient webClient = WebClient.create();

    @Override
    public Integer tryLocalPass(String password) {
        String result = webClient.post()
                .uri("http://localhost:8080/test/task3/answer")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue("{\"password\": \"" + password + "\"}")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        log.debug("PasswordGenerationServiceImpl: tryLocalPass answer: {}", result);
        return decodeAnswerToInt(result);
    }

    @Override
    public Integer tryPass(String password) {
        return null;
    }

    private int decodeAnswerToInt(String result) {
        switch (result) {
            case ">pass": {
                return 1;
            }
            case "<pass": {
                return -1;
            }
            default:
                return 0;
        }
    }
}
