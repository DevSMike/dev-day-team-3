package com.team3.holiday.util;

import com.google.gson.Gson;
import com.team3.holiday.dto.ServerAnswerErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;


@Slf4j
public class PasswordGeneratorRequestsUtil {

    private static final WebClient WEB_CLIENT = WebClient.create();

    public static Integer tryLocalPass(String password) {
        String result;
        try {
            result = WEB_CLIENT.post()
                    .uri("http://localhost:8080/test/task3/answer")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .bodyValue("{\"password\": \"" + password + "\"}")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (WebClientResponseException.BadRequest e) {

            Gson gson = new Gson();
            ServerAnswerErrorDto error = gson.fromJson(e.getResponseBodyAsString(), ServerAnswerErrorDto.class);
            result = error.getErrorMessage();
        }

        log.debug("PasswordGeneratorRequestsUtil: tryLocalPass answer: {}", result);

        if (result == null) result = "";

        return decodeAnswerToInt(result);
    }

    public static Integer tryPass(String password) {

        String result = "";
        try {
            result = WEB_CLIENT.post()
                    .uri("http://ya.praktikum.fvds.ru:8080/dev-day/task/3")
                    .header("AUTH_TOKEN", "e26d3434-c970-482a-b055-e2a55a364581")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .bodyValue("{\"password\": \"" + password + "\"}")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (WebClientResponseException.BadRequest e) {
            String errorMessage = e.getResponseBodyAsString();

            if (errorMessage.contains(">")) {
                result = errorMessage.substring(errorMessage.lastIndexOf(">"), errorMessage.lastIndexOf(">") + 5);
            } else if (errorMessage.contains("<")) {
                result = errorMessage.substring(errorMessage.lastIndexOf("<"), errorMessage.lastIndexOf("<") + 5);
            } else {
                log.error("Unsupported exceptions happens in PasswordGeneratorRequestsUtil tryPass(): {}", errorMessage);
            }
        }

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
