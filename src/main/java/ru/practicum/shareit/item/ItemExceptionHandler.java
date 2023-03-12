package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.item.exception.CommentCreateException;
import ru.practicum.shareit.item.exception.ItemCreateException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.user.dto.ErrorDto;

@RestControllerAdvice("ru.practicum.shareit")
@Slf4j
public class ItemExceptionHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleItemCreateException(final ItemCreateException e) {
        log.warn(e.getMessage());
        return new ResponseEntity<>(
                new ErrorDto("Ошибка при создании вещи", e.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleItemNotFoundException(final ItemNotFoundException e) {
        log.warn(e.getMessage());
        return new ResponseEntity<>(
                new ErrorDto("Вещь не найдена", e.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleMissingRequestHeaderException(
            final MissingRequestHeaderException e) {
        log.warn(e.getMessage());
        return new ResponseEntity<>(
                new ErrorDto("Отсутствует необходимы заголовок", e.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleCommentCreateException(final CommentCreateException e) {
        log.warn(e.getMessage());
        return new ResponseEntity<>(
                new ErrorDto("Ошибка при создании коментария", e.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }
}