package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.request.dto.ErrorDto;
import ru.practicum.shareit.exception.NotFoundException;

@RestControllerAdvice("ru.practicum.shareit.request")
@Slf4j
public class ItemRequestExceptionHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> itemRequestNotFoundExceptionHandler(NotFoundException e) {
        log.warn(e.getMessage());
        return new ResponseEntity<>(
                new ErrorDto("Реквест не найден", e.getMessage()),
                HttpStatus.NOT_FOUND);
    }
}
