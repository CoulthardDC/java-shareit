package ru.practicum.shareit.booking;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/bookings")
@Slf4j
@Tag(name = "Booking-контроллер",
        description = "Взаимодействие с бронью")
public class BookingController {
    private final BookingService bookingService;
    private static final String X_HEADER = "X-Sharer-User-Id";

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Operation(
            summary = "Создание брони",
            description = "Создание брони на вещь"
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BookingDto.class))
    )
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody BookingInputDto bookingDto,
                                    @RequestHeader(X_HEADER) long bookerId,
                                    HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: {} {}", request.getMethod(),
                request.getRequestURL());
        return new ResponseEntity<>(
                bookingService.addBooking(bookingDto, bookerId),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Редактирование статуса брони",
            description = "Редактирование статуса брони на вещь"
    )
    @ApiResponse(
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BookingDto.class))
    )
    @PatchMapping(value = "/{bookingId}")
    public ResponseEntity<?> update(@PathVariable(value = "bookingId")
                                        @Parameter(description = "id брони")
                                        Long bookingId,
                                    @RequestHeader(X_HEADER) Long userId,
                                    @RequestParam(value = "approved") Boolean approved,
                                    HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: {} {}", request.getMethod(),
                request.getRequestURL());
        return new ResponseEntity<>(
                bookingService.updateBooking(bookingId, userId, approved),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Получение брони по id",
            description = "Получение конкретной брони по id"
    )
    @ApiResponse(
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BookingDto.class))
    )
    @GetMapping(value = "/{bookingId}")
    public ResponseEntity<?> findBookingById(@PathVariable(value = "bookingId") Long bookingId,
                                             @RequestHeader(X_HEADER) Long userId,
                                             HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: {} {}", request.getMethod(),
                request.getRequestURL());
        return new ResponseEntity<>(
                bookingService.getBookingById(bookingId, userId),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Получение броней по параметру state",
            description = "Получение броней пользователя по параметру state"
    )
    @ApiResponse(
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = BookingDto.class)))
    )
    @GetMapping
    public ResponseEntity<?> findBookings(
            @RequestParam(value = "state", defaultValue = "ALL") String state,
            @RequestHeader(X_HEADER) Long userId,
            HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: {} {}", request.getMethod(),
                request.getRequestURL());
        return new ResponseEntity<>(
                bookingService.getBookings(state, userId),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Получение броней всех вещей владельца по параметру state",
            description = "Получение броней всех вещей владельцп по параметру state"
    )
    @ApiResponse(
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = BookingDto.class)))
    )
    @GetMapping(value = "/owner")
    public ResponseEntity<?> findBookingsOwner(
            @RequestParam(value = "state", defaultValue = "ALL") String state,
            @RequestHeader(X_HEADER) Long userId,
            HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: {} {}", request.getMethod(),
                request.getRequestURL());
        return new ResponseEntity<>(
                bookingService.getBookingsOwner(state, userId),
                HttpStatus.OK
        );
    }
}
