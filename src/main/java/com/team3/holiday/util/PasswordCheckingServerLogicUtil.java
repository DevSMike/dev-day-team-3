package com.team3.holiday.util;

import com.team3.holiday.dto.ServerAnswerDto;

public class PasswordCheckingServerLogicUtil {

    private static final char[] ANSWER = {'5', '5', 'C', 'f', 'c', 'e', 'f', '9'};

    public static String checkingPasswordByServer(String bodyValue) {

        String password = bodyValue.substring(bodyValue.lastIndexOf(":") + 3, bodyValue.lastIndexOf("\""));
        char[] charPass = password.toCharArray();

        for (int i = 0; i < password.length(); i++) {
            if (charPass[i] > ANSWER[i]) {
                return ">pass";
            } else if (charPass[i] < ANSWER[i]) {
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
