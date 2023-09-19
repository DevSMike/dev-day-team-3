package com.team3.holiday.controller;

import com.team3.holiday.dto.DevBodyDto;
import com.team3.holiday.dto.DevBodyInfo;
import com.team3.holiday.dto.ServerAnswerDto;
import com.team3.holiday.model.DevBody;
import com.team3.holiday.service.client.HttpClientService;
import com.team3.holiday.tasks.TaskFourHtml;
import com.team3.holiday.tasks.TaskThreeHtml;
import com.team3.holiday.tasks.TaskTwoHtml;
import com.team3.holiday.util.DecodeMessageCheckingServerLogicUtil;
import com.team3.holiday.util.PasswordCheckingServerLogicUtil;
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
        log.debug("HttpClientLocalController: making a register on task 1");
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
        log.debug("HttpClientLocalController: answer a register on task 1");
        DevBodyInfo answer = httpClientService.getRegistrationAnswer(body);

        log.info("Registration answer: " + answer);
        return Mono.just(answer);
    }

    @PostMapping("/task2")
    public String taskTwoDecodeMessage() {
        log.debug("HttpClientLocalController: making a  task 2");
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
        log.debug("HttpClientLocalController: asking for HTML on task 2");
        return TaskTwoHtml.getTempSecondTask();
    }

    @PostMapping("/task2/answer")
    public ServerAnswerDto taskTwoCheckDecodedMessage(@RequestBody String decodedMessage) {
        log.debug("HttpClientLocalController: checking decoded message on task 2");
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
        log.debug("HttpClientController: guessing a password");

        String pass = httpClientService.generateLocalPassword();
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
        log.debug("HttpClientLocalController: asking for HTML on task 3");
        return TaskThreeHtml.getTempThirdTask();
    }

    @PostMapping("/task3/answer")
    public String taskThreeCheckGeneratedPassword(@RequestBody String bodyValue) {
        log.debug("HttpClientLocalController: checking generated password on task 3");
        return PasswordCheckingServerLogicUtil.checkingPasswordByServer(bodyValue);
    }

    @PostMapping("/task4")
    public String sendDecodedString() {
        log.debug("HttpClientLocalController: congratulate with dev day");

        String htmlText = null;

        try {
            htmlText = client.get()
                    .uri("http://localhost:8080/test/task4/html")
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
                    .uri("http://localhost:8080/test/task4/answer")
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

    @GetMapping("/task4/html")
    public String taskFourGetFourthTask() {
        log.debug("HttpClientLocalController: asking for HTML on task 4");
        return TaskFourHtml.getTempFourTask();
    }

    @PostMapping("task4/answer")
    public String taskFourCheckDecodedMessage (@RequestBody String bodyValue) {
        log.debug("HttpClientLocalController: checking generated password on task 3");
        return DecodeMessageCheckingServerLogicUtil.checkingDecodingMessageByServer(bodyValue);
    }

}
