package ru.practicum.shareit.user.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.user.dto.ErrorDto;
import ru.practicum.shareit.user.exception.UserCreateException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.exception.UserRemoveException;

import javax.validation.ValidationException;

@RestControllerAdvice("ru.practicum.shareit.user.controller")
@Slf4j
public class UserExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
        public ErrorDto handleValidationException(final ValidationException e) {
            log.warn(e.getMessage());
            return new ErrorDto("Ошибка валидации", e.getMessage());
        }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto handleUserNotFoundException(final UserNotFoundException e) {
        log.warn(e.getMessage());
        return new ErrorDto("Пользователь не найден", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDto handleUserCreateException(final UserCreateException e) {
        log.warn(e.getMessage());
        return new ErrorDto("Ошибка при создании пользователя", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDto handleUserRemoveException(final UserRemoveException e) {
        log.warn(e.getMessage());
        return new ErrorDto("Ошибка при удалении пользователя", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleMethodArgumentNotValidException(
            final MethodArgumentNotValidException e) {
        log.warn("Ошибка валидации");
        return new ErrorDto("Ошибка валидации", e.getMessage());
    }
}
