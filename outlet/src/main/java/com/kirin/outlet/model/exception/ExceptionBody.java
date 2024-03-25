package com.kirin.outlet.model.exception;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExceptionBody {

    private String message;

    private LocalDateTime dateTime;

    public ExceptionBody(String message) {
        this.message = message;
        this.dateTime = LocalDateTime.now();
    }
}
