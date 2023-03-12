package ru.practicum.shareit.booking.exception;

public class BookingNotFoundException extends RuntimeException {
    public BookingNotFoundException(Long bookingId) {
        super(String.format("Бронирование с id = %d не найдено", bookingId));
    }
}
