package com.team3.holiday.controller;

import com.google.gson.Gson;
import com.team3.holiday.dto.DevBodyDto;
import com.team3.holiday.dto.DevBodyInfo;
import com.team3.holiday.dto.ServerAnswerDto;
import com.team3.holiday.dto.ServerAnswerErrorDto;
import com.team3.holiday.exception.BadServerAnswerException;
import com.team3.holiday.model.DevBody;
import com.team3.holiday.service.client.HttpClientService;
import com.team3.holiday.tasks.TaskFourHtml;
import com.team3.holiday.tasks.TaskThreeHtml;
import com.team3.holiday.tasks.TaskTwoHtml;
import com.team3.holiday.util.server.DecodeCaesarCipherServerLogic;
import com.team3.holiday.util.server.DecodeMessageCheckingServerLogicUtil;
import com.team3.holiday.util.server.PasswordCheckingServerLogicUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
@Slf4j
public class HttpClientLocalController {

    private final HttpClientService httpClientService;
    private final WebClient client = WebClient.create();

    //task 1 09/09/23
    @PostMapping("/task1")
    public DevBodyInfo taskOneMakeRegister(@RequestBody DevBody body) {
        log.debug("HttpClientLocalController: making a register on task 1");
        DevBodyDto bodyDto = httpClientService.registerUser(body);

        //async query
        DevBodyInfo info = null;
        try {
            info = client.post()
                    .uri("http://localhost:8080/test/task1/answer")
                    .header("MAIN_ANSWER", "42")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .bodyValue(bodyDto.getRequestBodyJson())
                    .retrieve()
                    .bodyToMono(DevBodyInfo.class)
                    .block();
        } catch (WebClientResponseException.BadRequest e) {
            Gson gson = new Gson();
            ServerAnswerErrorDto error = gson.fromJson(e.getResponseBodyAsString(), ServerAnswerErrorDto.class);
            throw new BadServerAnswerException(error.getErrorMessage());
        }
        return info;
    }

    @PostMapping("/task1/answer")
    public DevBodyInfo taskOneAnswerRegistration(@RequestBody DevBody body, HttpServletRequest servletRequest) {
        log.debug("HttpClientLocalController: answer a register on task 1");

        String mainAnswerHeader = servletRequest.getHeader("MAIN_ANSWER");
        DevBodyInfo answer = httpClientService.getRegistrationAnswer(body, mainAnswerHeader);

        log.info("Registration answer: " + answer);
        return answer;
    }

    @PostMapping("/task2")
    public ServerAnswerDto taskTwoDecodeMessage() {
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
        ServerAnswerDto result = null;

        try {
            result = client.post()
                    .uri("http://localhost:8080/test/task2/answer")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(decoded)
                    .retrieve()
                    .bodyToMono(ServerAnswerDto.class)
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
    public ServerAnswerDto taskTwoCheckDecodedMessage(@RequestBody String bodyValue) {
        log.debug("HttpClientLocalController: checking decoded message on task 2");
        return DecodeCaesarCipherServerLogic.checkingDecodeCaesarCipherByServer(bodyValue);
    }

    @PostMapping("/task3")
    public ServerAnswerDto generatePassword() {
        log.debug("HttpClientController: guessing a password");

        String pass = httpClientService.generateLocalPassword();
        ServerAnswerDto result = null;

        try {
            result = client.post()
                    .uri("http://localhost:8080/test/task3/answer")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .bodyValue("{\"password\": \"" + pass + "\"}")
                    .retrieve()
                    .bodyToMono(ServerAnswerDto.class)
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
    public ServerAnswerDto taskThreeCheckGeneratedPassword(@RequestBody String bodyValue) {
        log.debug("HttpClientLocalController: checking generated password on task 3");
        return PasswordCheckingServerLogicUtil.checkingPasswordByServer(bodyValue);
    }

    @PostMapping("/task4")
    public ServerAnswerDto sendDecodedString() {
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
        ServerAnswerDto result = null;

        try {
            result = client.post()
                    .uri("http://localhost:8080/test/task4/answer")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .bodyValue("{\"congratulation\": \"" + decoded + "\"}")
                    .retrieve()
                    .bodyToMono(ServerAnswerDto.class)
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
    public ServerAnswerDto taskFourCheckDecodedMessage (@RequestBody String bodyValue) {
        log.debug("HttpClientLocalController: checking generated password on task 3");
        return DecodeMessageCheckingServerLogicUtil.checkingDecodingMessageByServer(bodyValue);
    }

}
