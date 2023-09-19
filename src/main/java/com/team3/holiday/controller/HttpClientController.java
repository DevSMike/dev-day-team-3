package com.team3.holiday.controller;

import com.team3.holiday.dto.DevBodyDto;
import com.team3.holiday.dto.DevBodyInfo;
import com.team3.holiday.model.DevBody;
import com.team3.holiday.service.client.HttpClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;


@RestController
@Slf4j
@RequiredArgsConstructor
public class HttpClientController {

    private final HttpClientService httpClientService;
    private final WebClient client = WebClient.create();

    //task 1 09/09/23
    @PostMapping("/task1")
    public Mono<DevBodyInfo> makeRegister(@RequestBody DevBody body) {
        log.info("HttpClientController: making a register");
        DevBodyDto bodyDto = httpClientService.registerUser(body);

        //async query
        Mono<DevBodyInfo> info =  client.post()
                .uri("http://ya.praktikum.fvds.ru:8080/dev-day/register")
                .header("MAIN_ANSWER", "42")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(bodyDto.getRequestBodyJson())
                .retrieve()
                .bodyToMono(DevBodyInfo.class);
        log.info("Registration answer: " + info);
        return info;
    }

    //task 2 10/09/23
    @GetMapping("/task2")
    public String decodeMessage() {
        log.info("HttpClientController: task2 get HTML string code");
        String resultOfGet = null;

        try {
            resultOfGet = client.get()
                    .uri("http://ya.praktikum.fvds.ru:8080/dev-day/task/2")
                    .header("AUTH_TOKEN", "e26d3434-c970-482a-b055-e2a55a364581")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (WebClientResponseException.BadRequest ex) {
            String responseBody = ex.getResponseBodyAsString();
            log.debug(responseBody);
        }

        String decoded = httpClientService.decodeMessage(resultOfGet);
        String result = null;

        try {
            result = client.post()
                    .uri("http://ya.praktikum.fvds.ru:8080/dev-day/task/2")
                    .header("AUTH_TOKEN", "e26d3434-c970-482a-b055-e2a55a364581")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(decoded)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (WebClientResponseException.BadRequest ex) {
            String responseBody = ex.getResponseBodyAsString();
            log.debug(responseBody);
        }

        return result;
    }

    //task 3 11/09/23
    @PostMapping("/task3")
    public String generatePassword() {
        log.info("HttpClientController: guessing a password");

        String pass = httpClientService.generatePassword();
        //async query
        String result = null;
        try {
            result = client.post()
                    .uri("http://ya.praktikum.fvds.ru:8080/dev-day/task/3")
                    .header("AUTH_TOKEN", "e26d3434-c970-482a-b055-e2a55a364581")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .bodyValue("{\"password\": \"" + pass + "\"}")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (WebClientResponseException.BadRequest ex) {
            String responseBody = ex.getResponseBodyAsString();
            log.info(responseBody);
        }
        return result;
    }

    //task 4 12/09/23
    @PostMapping("/task4")
    public String sendDecodedString() {
        log.info("HttpClientController: congratulate with dev day");

        String htmlText = null;

        try {
            htmlText = client.get()
                    .uri("http://ya.praktikum.fvds.ru:8080/dev-day/task/4")
                    .header("AUTH_TOKEN", "e26d3434-c970-482a-b055-e2a55a364581")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (WebClientResponseException.BadRequest ex) {
            String responseBody = ex.getResponseBodyAsString();
            log.info(responseBody);
        }

        String decoded = httpClientService.decodeCongratulations(htmlText);
        String result = "";

        try {
            result = client.post()
                    .uri("http://ya.praktikum.fvds.ru:8080/dev-day/task/4")
                    .header("AUTH_TOKEN", "e26d3434-c970-482a-b055-e2a55a364581")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .bodyValue("{\"congratulation\": \"" + decoded + "\"}")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (WebClientResponseException.BadRequest ex) {
            String responseBody = ex.getResponseBodyAsString();
            log.info(responseBody);
        }

        return result;

    }
}

