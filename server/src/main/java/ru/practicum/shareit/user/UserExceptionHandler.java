package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.user.dto.ErrorDto;
import ru.practicum.shareit.exception.CreateException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.RemoveException;

@RestControllerAdvice("ru.practicum.shareit.user")
@Slf4j
public class UserExceptionHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto handleUserNotFoundException(final NotFoundException e) {
        log.warn(e.getMessage());
        return new ErrorDto("Пользователь не найден", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDto handleUserCreateException(final CreateException e) {
        log.warn(e.getMessage());
        return new ErrorDto("Ошибка при создании пользователя", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDto handleUserRemoveException(final RemoveException e) {
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
