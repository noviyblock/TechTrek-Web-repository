package com.startupgame.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class MlServiceUnavailableException extends RuntimeException {
    public MlServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}