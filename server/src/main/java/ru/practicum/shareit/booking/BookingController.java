package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingInputDto;


@RestController
@RequestMapping("/bookings")
@Slf4j
@Validated
public class BookingController {
    private final BookingService bookingService;
    private static final String X_HEADER = "X-Sharer-User-Id";

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody BookingInputDto bookingDto,
                                    @RequestHeader(X_HEADER) long bookerId) {
        log.info("Получен запрос к эндпоинту: {} /bookings", "POST");
        return new ResponseEntity<>(
                bookingService.addBooking(bookingDto, bookerId),
                HttpStatus.OK
        );
    }

    @PatchMapping(value = "/{bookingId}")
    public ResponseEntity<?> update(@PathVariable(value = "bookingId") Long bookingId,
                                    @RequestHeader(X_HEADER) Long userId,
                                    @RequestParam(value = "approved") Boolean approved) {
        log.info("Получен запрос к эндпоинту: {} /bookings/{}", "PATCH", bookingId);
        return new ResponseEntity<>(
                bookingService.updateBooking(bookingId, userId, approved),
                HttpStatus.OK
        );
    }

    @GetMapping(value = "/{bookingId}")
    public ResponseEntity<?> findBookingById(@PathVariable(value = "bookingId") Long bookingId,
                                             @RequestHeader(X_HEADER) Long userId) {
        log.info("Получен запрос к эндпоинту: {} /bookings/{}", "GET", bookingId);
        return new ResponseEntity<>(
                bookingService.getBookingById(bookingId, userId),
                HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<?> findBookings(
            @RequestParam(value = "state", defaultValue = "ALL") String state,
            @RequestHeader(X_HEADER) Long userId,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size) {
        log.info("Получен запрос к эндпоинту: {} /bookings", "GET");
        return new ResponseEntity<>(
                bookingService.getBookings(state, userId, from, size),
                HttpStatus.OK
        );
    }

    @GetMapping(value = "/owner")
    public ResponseEntity<?> findBookingsOwner(
            @RequestParam(value = "state", defaultValue = "ALL") String state,
            @RequestHeader(X_HEADER) Long userId,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size) {
        log.info("Получен запрос к эндпоинту: {} /bookings/owner", "GET");
        return new ResponseEntity<>(
                bookingService.getBookingsOwner(state, userId, from, size),
                HttpStatus.OK
        );
    }
}
