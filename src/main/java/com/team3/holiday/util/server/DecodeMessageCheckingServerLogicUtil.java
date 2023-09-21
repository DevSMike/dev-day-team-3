package com.team3.holiday.util.server;

import com.team3.holiday.dto.ServerAnswerDto;
import com.team3.holiday.exception.BadServerAnswerException;

public class DecodeMessageCheckingServerLogicUtil {

    private static final String ANSWER = "Широкая электрификация";

    public static ServerAnswerDto checkingDecodingMessageByServer(String bodyValue) {

        String message = bodyValue.substring(bodyValue.lastIndexOf(":") + 3, bodyValue.lastIndexOf("\""));
        if (message.equals(ANSWER)) {
            return ServerAnswerDto.builder()
                    .completed(true)
                    .message("У вас получилось правильно разгадать слово. С днем программиста ^^")
                    .nextTaskUrl("bolche net)")
                    .build();
        } else {
             throw new BadServerAnswerException("Неправильно или не успели!");
        }
    }
}
