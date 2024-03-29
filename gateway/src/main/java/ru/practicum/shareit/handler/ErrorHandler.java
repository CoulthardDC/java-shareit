package ru.practicum.shareit.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("Ошибка валидации");
        return new ResponseEntity<>(
                new ErrorDto(e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
