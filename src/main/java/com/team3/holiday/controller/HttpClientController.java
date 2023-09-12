package com.team3.holiday.controller;

import com.team3.holiday.dto.DevBodyDto;
import com.team3.holiday.dto.DevBodyInfoDto;
import com.team3.holiday.model.DevBody;
import com.team3.holiday.service.HttpClientService;
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

import java.io.UnsupportedEncodingException;


@RestController
@Slf4j
@RequiredArgsConstructor
public class HttpClientController {

    private final HttpClientService httpClientService;
    private final WebClient client = WebClient.create();

    //task 1 09/09/23
    @PostMapping("/task1")
    public Mono<DevBodyInfoDto> makeRegister(@RequestBody DevBody body) {
        log.info("HttpClientController: making a register");
        DevBodyDto bodyDto = httpClientService.registerUser(body);

        //async query
        return client.post()
                .uri("http://localhost:8080/task1/answer")
                .header("MAIN_ANSWER", "42")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(bodyDto.getRequestBodyJson())
                .retrieve()
                .bodyToMono(DevBodyInfoDto.class);
    }

    @PostMapping("/task1/answer")
    public Mono<DevBodyInfoDto> answerRegistration(@RequestBody DevBody body) {
        log.info("HttpClientController: answer a register");
        DevBodyInfoDto answer = httpClientService.getRegistrationAnswer(body);

        log.info("Registration answer: " + answer);
        return Mono.just(answer);
    }

    //task 2 10/09/23
    @GetMapping("/task2/get")
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
            log.info(responseBody);
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
            if (responseBody.contains("<span>Ваша команда &quot;Team3&quot; уже выполнила Задание 2</span>")) {
                result = "{\"completed\":true,\"message\":\"Вы успели! Настоящие программисты всегда на шаг " +
                        "впереди железяк:)\",\"nextTaskUrl\":\"http://ya.praktikum.fvds.ru:8080/dev-day/task/3\"}";
            }
        }

        return result;
    }

    //task 3 11/09/23
    @PostMapping("/task3")
    public String generatePassword() {
        log.info("HttpClientController: making a register");

        String pass = httpClientService.generatePassword();
        log.info(pass);
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

    @GetMapping("/task4")
    public String getAnStringCoded() throws UnsupportedEncodingException {
        log.info("HttpClientController: getting a coded string");


//        //async query
//        return client.get()
//                .uri("http://localhost:8080/task1/answer")
////                .header("MAIN_ANSWER", "42")
//                .accept(MediaType.APPLICATION_JSON)
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();


        String url = "https://2cyr.com/decode/?lang=ru";
        String text = "Øèðîêàÿ ýëåêòðèôèêàöèÿ";
        String response = client.post()
                .uri(url)
                .bodyValue("text=" + text)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        //String coded = "Øèðîêàÿ ýëåêòðèôèêàöèÿ";
       // String decoded = httpClientService.decodeCongratulations(coded);
        return  "";

    }

//    @PostMapping("/task4")
//    public String sendDecodedString() {
//        log.info("HttpClientController: send a decoded string");
//
//
//    }
}

