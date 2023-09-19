package com.team3.holiday.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;


@Slf4j
public class PasswordGeneratorRequestsUtil  {

    private static final WebClient WEB_CLIENT = WebClient.create();

    public static Integer tryLocalPass(String password) {
        String result = WEB_CLIENT.post()
                .uri("http://localhost:8080/test/task3/answer")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue("{\"password\": \"" + password + "\"}")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        log.debug("PasswordGeneratorRequestsUtil: tryLocalPass answer: {}", result);

        if (result == null) result = "";

        return decodeAnswerToInt(result);
    }

    public static Integer tryPass(String password) {

        String result = WEB_CLIENT.post()
                .uri("http://ya.praktikum.fvds.ru:8080/dev-day/task/3")
                .header("AUTH_TOKEN", "e26d3434-c970-482a-b055-e2a55a364581")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue("{\"password\": \"" + password + "\"}")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        log.debug("PasswordGeneratorRequestsUtil: tryPass answer: {}", result);

        if (result == null) result = "";

        return decodeAnswerToInt(result);
    }

    private static int decodeAnswerToInt(String result) {
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
