package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.CommentCreateException;
import ru.practicum.shareit.exception.CreateException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.ErrorDto;

@RestControllerAdvice("ru.practicum.shareit.item")
@Slf4j
public class ItemExceptionHandler {
    @ExceptionHandler(CreateException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleItemCreateException(final CreateException e) {
        log.warn(e.getMessage());
        return new ResponseEntity<>(
                new ErrorDto("Ошибка при создании", e.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleItemNotFoundException(final NotFoundException e) {
        log.warn(e.getMessage());
        return new ResponseEntity<>(
                new ErrorDto("Не найдено", e.getMessage()),
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

    @ExceptionHandler(CommentCreateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleCommentCreateException(
            final CommentCreateException e) {
        log.warn(e.getMessage());
        return new ResponseEntity<>(
                new ErrorDto("Ошибка при добавлении коментария", e.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }
}