package com.flightproviderconsumer.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExceptionResponse {
    private int status;
    private String message;
    private LocalDateTime dateTime;

    public ExceptionResponse(String message) {
        this.message = message;
    }
}