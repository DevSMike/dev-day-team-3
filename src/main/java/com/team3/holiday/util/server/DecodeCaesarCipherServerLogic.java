package com.team3.holiday.util.server;

import com.team3.holiday.dto.ServerAnswerDto;
import com.team3.holiday.exception.BadServerAnswerException;

public class DecodeCaesarCipherServerLogic {

    private static final String ANSWER = "HAVE A FINE REACT CODING DAY";

    public static ServerAnswerDto checkingDecodeCaesarCipherByServer(String bodyValue) {

        //String answer = "{\"decoded\": \"HAVE A FINE REACT CODING DAY\"}";

        String decoded = bodyValue.substring(bodyValue.lastIndexOf(":") + 3, bodyValue.lastIndexOf("\""));

        if (decoded.equals(ANSWER)) {
            return ServerAnswerDto.builder()
                    .completed(true)
                    .message("Вы успели! Настоящие программисты всегда на шаг впереди железяк:)")
                    .nextTaskUrl("http://ya.praktikum.fvds.ru:8080/dev-day/task/3")
                    .build();
        } else {
            throw new BadServerAnswerException("Неправильно или не успели)");
        }
    }
}
