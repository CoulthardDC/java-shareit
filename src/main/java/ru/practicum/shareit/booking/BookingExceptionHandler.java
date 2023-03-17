package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.PermissionException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.ErrorDto;

import javax.validation.ValidationException;

@RestControllerAdvice("ru.practicum.shareit.booking")
@Slf4j
public class BookingExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleBookingNotFoundException(final NotFoundException e) {
        log.warn(e.getMessage());
        return new ResponseEntity<>(
                new ErrorDto("Бронирование не найдено", e.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleValidationException(final ValidationException e) {
        log.warn(e.getMessage());
        return new ResponseEntity<>(
                new ErrorDto(e.getMessage(), "Ошибка валидвации"),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handlePermissionException(final PermissionException e) {
        log.warn(e.getMessage());
        return new ResponseEntity<>(
                new ErrorDto("Ошибка доступа", e.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }
}
