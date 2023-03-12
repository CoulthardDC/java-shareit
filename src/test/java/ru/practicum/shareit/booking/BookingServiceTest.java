package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.exception.PermissionException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceTest {
    private final BookingService bookingService;
    private final UserService userService;
    private final ItemService itemService;

    private final UserDto userDto = UserDto
            .builder()
            .id(300L)
            .name("first")
            .email("first@first300.ru")
            .build();
    private final UserDto userDto1 = UserDto
            .builder()
            .id(301L)
            .email("example1@example.com")
            .name("name1")
            .build();
    private final UserDto userDto2 = UserDto
            .builder()
            .id(302L)
            .email("example2@example.com")
            .name("name2")
            .build();

    private final ItemDto itemDto1 = ItemDto
            .builder()
            .id(301L)
            .name("item1")
            .description("description1")
            .owner(userDto)
            .available(true)
            .build();

    @Test
    void shouldExceptionWhenCreateBookingByOwnerItem() {
        UserDto ownerDto = userService.addUser(userDto1);
        ItemDto newItemDto = itemService.addItem(itemDto1, ownerDto.getId());
        BookingInputDto bookingInputDto = BookingInputDto
                .builder()
                .itemId(newItemDto.getId())
                .start(LocalDateTime.of(2030, 12, 25, 12, 0, 0))
                .end(LocalDateTime.of(2030, 12, 26, 12, 0, 0))
                .build();

        PermissionException exp = assertThrows(PermissionException.class,
                () -> bookingService.addBooking(bookingInputDto, ownerDto.getId()));
        assertEquals("Вещь с ID=" + newItemDto.getId() + " недоступна для бронирования самим владельцем!",
                exp.getMessage());
    }

    @Test
    void shouldExceptionWhenGetBookingByNotOwnerOrNotBooker() {
        UserDto ownerDto = userService.addUser(userDto1);
        UserDto newUserDto = userService.addUser(userDto2);
        UserDto userDto3 = UserDto
                .builder()
                .id(303L)
                .name("name3")
                .email("example3@example.ru")
                .build();

        userDto3 = userService.addUser(userDto3);
        Long userId = userDto3.getId();
        ItemDto newItemDto = itemService.addItem(itemDto1, ownerDto.getId());

        BookingInputDto bookingInputDto = BookingInputDto
                .builder()
                .itemId(newItemDto.getId())
                .start(LocalDateTime.of(2030, 12, 25, 12, 0, 0))
                .end(LocalDateTime.of(2030, 12, 26, 12, 0, 0))
                .build();

        BookingDto bookingDto = bookingService.addBooking(bookingInputDto, newUserDto.getId());
        assertThrows(UserNotFoundException.class,
                () -> bookingService.getBookingById(bookingDto.getId(), userId));
    }

    @Test
    void shouldReturnBookingsWhenGetBookingsByBookerAndSizeIsNotNull() {
        UserDto ownerDto = userService.addUser(userDto1);
        UserDto newUserDto = userService.addUser(userDto2);
        ItemDto newItemDto = itemService.addItem(itemDto1, ownerDto.getId());
        BookingInputDto bookingInputDto = BookingInputDto
                .builder()
                .itemId(newItemDto.getId())
                .start(LocalDateTime.of(2030, 12, 25, 12, 0, 0))
                .end(LocalDateTime.of(2030, 12, 26, 12, 0, 0))
                .build();
        bookingService.addBooking(bookingInputDto, newUserDto.getId());
        BookingInputDto bookingInputDto1 = BookingInputDto
                .builder()
                .itemId(newItemDto.getId())
                .start(LocalDateTime.of(2031, 12, 25, 12, 0, 0))
                .end(LocalDateTime.of(2031, 12, 26, 12, 0, 0))
                .build();
        bookingService.addBooking(bookingInputDto1, newUserDto.getId());
        List<BookingDto> listBookings = bookingService.getBookings("ALL", newUserDto.getId(), 0, 1);
        assertEquals(1, listBookings.size());
    }

    @Test
    void shouldReturnBookingsWhenGetBookingsInWaitingStatusByBookerAndSizeNotNull() {
        UserDto ownerDto = userService.addUser(userDto1);
        UserDto newUserDto = userService.addUser(userDto2);
        ItemDto newItemDto = itemService.addItem(itemDto1, ownerDto.getId());
        BookingInputDto bookingInputDto = BookingInputDto
                .builder()
                .itemId(newItemDto.getId())
                .start(LocalDateTime.of(2030, 12, 25, 12, 0, 0))
                .end(LocalDateTime.of(2030, 12, 26, 12, 0, 0))
                .build();
        bookingService.addBooking(bookingInputDto, newUserDto.getId());
        BookingInputDto bookingInputDto1 = BookingInputDto
                .builder()
                .itemId(newItemDto.getId())
                .start(LocalDateTime.of(2031, 12, 25, 12, 0, 0))
                .end(LocalDateTime.of(2031, 12, 26, 12, 0, 0))
                .build();
        bookingService.addBooking(bookingInputDto1, newUserDto.getId());
        List<BookingDto> listBookings = bookingService.getBookings("WAITING", newUserDto.getId(),
                0, 1);
        assertEquals(1, listBookings.size());
    }

    @Test
    void shouldReturnBookingsWhenGetBookingsInRejectedStatusByBookerAndSizeNotNull() {
        UserDto ownerDto = userService.addUser(userDto1);
        UserDto newUserDto = userService.addUser(userDto2);
        ItemDto newItemDto = itemService.addItem(itemDto1, ownerDto.getId());
        BookingInputDto bookingInputDto = BookingInputDto
                .builder()
                .itemId(newItemDto.getId())
                .start(LocalDateTime.of(2030, 12, 25, 12, 0, 0))
                .end(LocalDateTime.of(2030, 12, 26, 12, 0, 0))
                .build();
        bookingService.addBooking(bookingInputDto, newUserDto.getId());
        BookingInputDto bookingInputDto1 = BookingInputDto
                .builder()
                .itemId(newItemDto.getId())
                .start(LocalDateTime.of(2031, 12, 25, 12, 0, 0))
                .end(LocalDateTime.of(2031, 12, 26, 12, 0, 0))
                .build();
        bookingService.addBooking(bookingInputDto1, newUserDto.getId());
        List<BookingDto> listBookings = bookingService.getBookings("REJECTED", newUserDto.getId(),
                0, 1);
        assertEquals(0, listBookings.size());
    }

    @Test
    void shouldReturnBookingsWhenGetBookingsByOwnerAndSizeNotNull() {
        UserDto ownerDto = userService.addUser(userDto1);
        UserDto newUserDto = userService.addUser(userDto2);
        ItemDto newItemDto = itemService.addItem(itemDto1, ownerDto.getId());
        BookingInputDto bookingInputDto = BookingInputDto
                .builder()
                .itemId(newItemDto.getId())
                .start(LocalDateTime.of(2030, 12, 25, 12, 0, 0))
                .end(LocalDateTime.of(2030, 12, 26, 12, 0, 0))
                .build();
        bookingService.addBooking(bookingInputDto, newUserDto.getId());
        BookingInputDto bookingInputDto1 = BookingInputDto
                .builder()
                .itemId(newItemDto.getId())
                .start(LocalDateTime.of(2031, 12, 25, 12, 0, 0))
                .end(LocalDateTime.of(2031, 12, 26, 12, 0, 0))
                .build();
        bookingService.addBooking(bookingInputDto1, newUserDto.getId());
        List<BookingDto> listBookings = bookingService.getBookingsOwner("ALL", ownerDto.getId(),
                0, 1);
        assertEquals(1, listBookings.size());
    }

    @Test
    void shouldReturnBookingsWhenGetBookingsByOwnerAndStatusWaitingAndSizeNotNull() {
        UserDto ownerDto = userService.addUser(userDto1);
        UserDto newUserDto = userService.addUser(userDto2);
        ItemDto newItemDto = itemService.addItem(itemDto1, ownerDto.getId());
        BookingInputDto bookingInputDto = BookingInputDto
                .builder()
                .itemId(newItemDto.getId())
                .start(LocalDateTime.of(2030, 12, 25, 12, 0, 0))
                .end(LocalDateTime.of(2030, 12, 26, 12, 0, 0))
                .build();
        bookingService.addBooking(bookingInputDto, newUserDto.getId());
        BookingInputDto bookingInputDto1 = BookingInputDto
                .builder()
                .itemId(newItemDto.getId())
                .start(LocalDateTime.of(2031, 12, 25, 12, 0, 0))
                .end(LocalDateTime.of(2031, 12, 26, 12, 0, 0))
                .build();
        bookingService.addBooking(bookingInputDto1, newUserDto.getId());
        List<BookingDto> listBookings = bookingService.getBookingsOwner("WAITING", ownerDto.getId(),
                0, 1);
        assertEquals(1, listBookings.size());
    }

    @Test
    void shouldReturnBookingsWhenGetBookingsByOwnerAndStatusRejectedAndSizeNotNull() {
        UserDto ownerDto = userService.addUser(userDto1);
        UserDto newUserDto = userService.addUser(userDto2);
        ItemDto newItemDto = itemService.addItem(itemDto1, ownerDto.getId());
        BookingInputDto bookingInputDto = BookingInputDto
                .builder()
                .itemId(newItemDto.getId())
                .start(LocalDateTime.of(2030, 12, 25, 12, 0, 0))
                .end(LocalDateTime.of(2030, 12, 26, 12, 0, 0))
                .build();
        bookingService.addBooking(bookingInputDto, newUserDto.getId());
        BookingInputDto bookingInputDto1 = BookingInputDto
                .builder()
                .itemId(newItemDto.getId())
                .start(LocalDateTime.of(2031, 12, 25, 12, 0, 0))
                .end(LocalDateTime.of(2031, 12, 26, 12, 0, 0))
                .build();
        bookingService.addBooking(bookingInputDto1, newUserDto.getId());
        List<BookingDto> listBookings = bookingService.getBookingsOwner("REJECTED", ownerDto.getId(),
                0, 1);
        assertEquals(0, listBookings.size());
    }
}