package com.team3.holiday.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ServerAnswerErrorDto {

    private Boolean completed;
    private String errorMessage;
    private HttpStatus codeError;
}
