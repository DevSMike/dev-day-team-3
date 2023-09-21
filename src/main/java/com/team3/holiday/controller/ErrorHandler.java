package com.team3.holiday.controller;

import com.team3.holiday.dto.ServerAnswerErrorDto;
import com.team3.holiday.exception.BadServerAnswerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler(BadServerAnswerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ServerAnswerErrorDto handleBadServerAnswerException(BadServerAnswerException e) {
        log.error(e.getMessage());
        return ServerAnswerErrorDto.builder()
                .codeError(HttpStatus.BAD_REQUEST)
                .errorMessage(e.getMessage())
                .completed(false)
                .build();
    }
}
