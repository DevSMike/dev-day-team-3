package com.team3.holiday.util.server;

import com.team3.holiday.dto.ServerAnswerDto;
import com.team3.holiday.exception.BadServerAnswerException;

public class PasswordCheckingServerLogicUtil {

    private static final char[] ANSWER = {'5', '5', 'C', 'f', 'c', 'e', 'f', '9'};

    public static ServerAnswerDto checkingPasswordByServer(String bodyValue) {

        String password = bodyValue.substring(bodyValue.lastIndexOf(":") + 3, bodyValue.lastIndexOf("\""));
        char[] charPass = password.toCharArray();

        for (int i = 0; i < password.length(); i++) {
            if (charPass[i] > ANSWER[i]) {
//                return ">pass";
                throw new BadServerAnswerException(">pass");
            } else if (charPass[i] < ANSWER[i]) {
//                return "<pass";
                throw new BadServerAnswerException("<pass");
            }
        }

        return ServerAnswerDto.builder()
                .completed(true)
                .message("Получилось! Вы бы точно спасли Зион ^.^")
                .nextTaskUrl("http://ya.praktikum.fvds.ru:8080/dev-day/task/4")
                .build();
    }
}
