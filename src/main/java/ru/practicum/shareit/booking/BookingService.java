package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputDto;

import java.util.List;

public interface BookingService {
    public BookingDto addBooking(BookingInputDto bookingDto, Long bookerId);

    public BookingDto updateBooking(Long bookingId, Long userId, Boolean approved);

    public BookingDto getBookingById(Long bookingId, Long userId);

    public List<BookingDto> getBookings(String state, Long userId, Integer from, Integer size);

    public List<BookingDto> getBookingsOwner(String state, Long ownerId, Integer from, Integer size);
}
