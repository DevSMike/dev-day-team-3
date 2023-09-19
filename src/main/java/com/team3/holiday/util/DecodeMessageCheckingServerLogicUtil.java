package com.team3.holiday.util;

import com.team3.holiday.dto.ServerAnswerDto;
import com.team3.holiday.dto.ServerAnswerErrorDto;

public class DecodeMessageCheckingServerLogicUtil {

    private static final String ANSWER = "Широкая электрификация";

    public static String checkingDecodingMessageByServer(String bodyValue) {

        String message = bodyValue.substring(bodyValue.lastIndexOf(":") + 3, bodyValue.lastIndexOf("\""));
        if (message.equals(ANSWER)) {
            return ServerAnswerDto.builder()
                    .completed(true)
                    .message("У вас получилось правильно разгадать слово. С днем программиста ^^")
                    .nextTaskUrl("bolche net)")
                    .build().toString();
        } else {
            return ServerAnswerErrorDto.builder()
                    .errorMessage("Неправильно или не успели!")
                    .completed(false)
                    .codeError(400)
                    .build().toString();
        }
    }
}
