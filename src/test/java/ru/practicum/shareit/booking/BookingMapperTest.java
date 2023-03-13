package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

public class BookingMapperTest {
    private final BookingMapper bookingMapper = new BookingMapperImpl();

    UserDto ownerDto = UserDto
            .builder()
            .id(1L)
            .name("ownerName")
            .email("owner@email.com")
            .build();

    UserDto bookerDto = UserDto
            .builder()
            .id(2L)
            .name("bookerName")
            .email("booker@email.com")
            .build();

    ItemDto itemDto = ItemDto
            .builder()
            .id(1L)
            .name("itemName")
            .description("itemDescription")
            .owner(ownerDto)
            .available(true)
            .build();

    BookingDto bookingDto = BookingDto
            .builder()
            .id(1L)
            .booker(bookerDto)
            .item(itemDto)
            .start(LocalDateTime.now())
            .end(LocalDateTime.now())
            .build();

    BookingShortDto bookingShortDto = BookingShortDto
            .builder()
            .id(2L)
            .bookerId(2L)
            .start(LocalDateTime.now())
            .end(LocalDateTime.now())
            .build();

    User owner = User
            .builder()
            .id(1L)
            .name("ownerName")
            .email("owner@email.com")
            .build();

    User booker = User
            .builder()
            .id(2L)
            .name("bookerName")
            .email("booker@email.com")
            .build();

    Item item = Item
            .builder()
            .id(1L)
            .name("itemName")
            .description("itemDescription")
            .owner(owner)
            .available(true)
            .build();

    Booking booking = Booking
            .builder()
            .id(1L)
            .start(LocalDateTime.now())
            .end(LocalDateTime.now())
            .booker(booker)
            .item(item)
            .build();

    @Test
    public void testBookingToBookingDtoWhenBookingIsNull() {
        BookingDto newBookingDto = bookingMapper.toBookingDto(null);

        Assertions.assertNull(newBookingDto);
    }

    @Test
    public void testBookingToBookingShortDtoWhenBookingIsNull() {
        BookingShortDto newBookingShortDto = bookingMapper.toBookingShortDto(null);

        Assertions.assertNull(newBookingShortDto);
    }

    @Test
    public void testBookingToBookingShortDto() {
        BookingShortDto newBookingShortDto = bookingMapper.toBookingShortDto(booking);

        Assertions.assertEquals(1L, newBookingShortDto.getId());
    }
}
