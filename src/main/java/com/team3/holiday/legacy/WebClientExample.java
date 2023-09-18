package com.team3.holiday.legacy;

import org.springframework.boot.SpringApplication;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;


//legacy endless requesting class
public class WebClientExample {
    public static void main(String[] args) {
        SpringApplication.run(WebClientExample.class, args);

        WebClient webClient = WebClient.create();
        String uri = "http://ya.praktikum.fvds.ru:8080/dev-day/task/4";
        String authToken = "e26d3434-c970-482a-b055-e2a55a364581";
        String requestBody = "{\"congratulation\":\"Поздравляем суперскую команду с Днем опытного Программиста\"}";

        while (true) {
            try {
                String responseBody = webClient.post()
                        .uri(uri)
                        .header("AUTH_TOKEN", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(requestBody))
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();
                System.out.println(responseBody);
                if (responseBody.contains("200")) {
                    break;
                }
            } catch (WebClientResponseException e) {
                System.out.println(e.getMessage());
            }
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
