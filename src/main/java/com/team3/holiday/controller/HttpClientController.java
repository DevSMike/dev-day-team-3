package com.team3.holiday.controller;

import com.team3.holiday.dto.DevBodyDto;
import com.team3.holiday.dto.DevBodyInfoDto;
import com.team3.holiday.model.DevBody;
import com.team3.holiday.service.HttpClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/register")
public class HttpClientController {

    private final HttpClientService httpClientService;
    private final WebClient client = WebClient.create();

    //task 1 09/09/23
    @PostMapping()
    public Mono<DevBodyInfoDto> makeRegister(@RequestBody DevBody body) {
        log.info("HttpClientController: making a register");
        DevBodyDto bodyDto = httpClientService.registerUser(body);

        //async query
        return client.post()
                .uri("http://localhost:8080/register/post")
                .header("MAIN_ANSWER", "42")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(bodyDto.getRequestBodyJson())
                .retrieve()
                .bodyToMono(DevBodyInfoDto.class);
    }

    @PostMapping("/post")
    public Mono<DevBodyInfoDto> answerRegistration(@RequestBody DevBody body) {
        log.info("HttpClientController: answer a register");
        DevBodyInfoDto answer = httpClientService.getRegistrationAnswer(body);

        log.info("Registration answer: " + answer);
        return Mono.just(answer);
    }


}

