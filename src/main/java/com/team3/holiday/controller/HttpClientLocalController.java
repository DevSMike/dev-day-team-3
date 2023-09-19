package com.team3.holiday.controller;

import com.team3.holiday.dto.DevBodyDto;
import com.team3.holiday.dto.DevBodyInfo;
import com.team3.holiday.dto.ServerAnswerDto;
import com.team3.holiday.model.DevBody;
import com.team3.holiday.service.client.HttpClientService;
import com.team3.holiday.tasks.TaskThreeHtml;
import com.team3.holiday.tasks.TaskTwoHtml;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
@Slf4j
public class HttpClientLocalController {

    private final HttpClientService httpClientService;
    private final WebClient client = WebClient.create();

    //task 1 09/09/23
    @PostMapping("/task1")
    public Mono<DevBodyInfo> taskOneMakeRegister(@RequestBody DevBody body) {
        log.info("HttpClientLocalController: making a register on task 1");
        DevBodyDto bodyDto = httpClientService.registerUser(body);

        //async query
        return client.post()
                .uri("http://localhost:8080/task1/answer")
                .header("MAIN_ANSWER", "42")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(bodyDto.getRequestBodyJson())
                .retrieve()
                .bodyToMono(DevBodyInfo.class);
    }

    @PostMapping("/task1/answer")
    public Mono<DevBodyInfo> taskOneAnswerRegistration(@RequestBody DevBody body) {
        log.info("HttpClientLocalController: answer a register on task 1");
        DevBodyInfo answer = httpClientService.getRegistrationAnswer(body);

        log.info("Registration answer: " + answer);
        return Mono.just(answer);
    }

    @PostMapping("/task2")
    public String taskTwoDecodeMessage() {
        log.info("HttpClientLocalController: making a  task 2");
        String resultOfGet = null;

        try {
            resultOfGet = client.get()
                    .uri("http://localhost:8080/test/task2/html")
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
                    .uri("http://localhost:8080/test/task2/answer")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(decoded)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (WebClientResponseException.BadRequest ex) {
            String responseBody = ex.getResponseBodyAsString();
            log.info(responseBody);
        }

        return result;
    }

    @GetMapping("/task2/html")
    public String taskTwoGetHtmlSecondTask() {
        log.info("HttpClientLocalController: asking for HTML on task 2");
        return TaskTwoHtml.getTempSecondTask();
    }

    @PostMapping("/task2/answer")
    public ServerAnswerDto taskTwoCheckDecodedMessage(@RequestBody String decodedMessage) {
        log.info("HttpClientLocalController: checking decoded message on task 2");
        String answer = "{\"decoded\": \"HAVE A FINE REACT CODING DAY\"}";

        if (decodedMessage.equals(answer)) {
            return ServerAnswerDto.builder()
                    .completed(true)
                    .message("Вы успели! Настоящие программисты всегда на шаг впереди железяк:)")
                    .nextTaskUrl("http://ya.praktikum.fvds.ru:8080/dev-day/task/3")
                    .build();
        } else {
            throw new RuntimeException("BAD REQUEST");
        }
    }

    @PostMapping("/task3")
    public String generatePassword() {
        log.info("HttpClientController: guessing a password");

        String pass = httpClientService.generatePassword();
        //async query
        String result = null;
        try {
            result = client.post()
                    .uri("http://localhost:8080/test/task3/answer")
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


    @GetMapping("/task3/html")
    public String taskThreeGetThirdTask() {
        log.info("HttpClientLocalController: asking for HTML on task 3");
        return TaskThreeHtml.getTempThirdTask();
    }


    @PostMapping("/task3/answer")
    public String taskThreeCheckGeneratedPassword(@RequestBody String bodyValue) {
        log.info("HttpClientLocalController: checking generated password on task 3");
        char[] answer = { '5', '5', 'C', 'f', 'c', 'e', 'f', '9'};

        String password = bodyValue.substring(bodyValue.lastIndexOf(":") + 3, bodyValue.lastIndexOf("\""));
        char[] charPass = password.toCharArray();

        for (int i = 0; i < password.length(); i++) {
            if (charPass[i] > answer[i]) {
                return ">pass";
            } else if (charPass[i] < answer[i]) {
                return "<pass";
            }
        }
        ServerAnswerDto serverAnswer = ServerAnswerDto.builder()
                .completed(true)
                .message("Получилось! Вы бы точно спасли Зион ^.^")
                .nextTaskUrl("http://ya.praktikum.fvds.ru:8080/dev-day/task/4")
                .build();

        return serverAnswer.toString();
    }


}
