package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.PermissionException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputDto;

import javax.validation.ValidationException;
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
    void shouldExceptionWhenCreateBookingAndEndBeforeStart() {
        UserDto ownerDto = userService.addUser(userDto1);
        ItemDto newItemDto = itemService.addItem(itemDto1, ownerDto.getId());
        UserDto newUserDto = userService.addUser(userDto2);
        BookingInputDto bookingInputDto = BookingInputDto
                .builder()
                .itemId(newItemDto.getId())
                .start(LocalDateTime.of(2030, 12, 26, 12, 0, 0))
                .end(LocalDateTime.of(2030, 12, 25, 12, 0, 0))
                .build();

        ValidationException exp = assertThrows(ValidationException.class,
                () -> bookingService.addBooking(bookingInputDto, newUserDto.getId()));
        assertEquals("Время начала аренды позже времени окончания",
                exp.getMessage());
    }

    @Test
    void shouldExceptionWhenUpdateAfterEnd() {
        UserDto ownerDto = userService.addUser(userDto1);
        ItemDto newItemDto = itemService.addItem(itemDto1, ownerDto.getId());
        UserDto newUserDto = userService.addUser(userDto2);
        BookingInputDto bookingInputDto = BookingInputDto
                .builder()
                .itemId(newItemDto.getId())
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusSeconds(3))
                .build();
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            return;
        }
        BookingDto bookingDto = bookingService.addBooking(bookingInputDto, newUserDto.getId());

        ValidationException exp = assertThrows(ValidationException.class,
                () -> bookingService.updateBooking(bookingDto.getId(), ownerDto.getId(), true));
        assertEquals("Время бронирования истекло",
                exp.getMessage());
    }

    @Test
    void shouldCancelBookingAfterUpdate() {
        UserDto ownerDto = userService.addUser(userDto1);
        ItemDto newItemDto = itemService.addItem(itemDto1, ownerDto.getId());
        UserDto newUserDto = userService.addUser(userDto2);
        BookingInputDto bookingInputDto = BookingInputDto
                .builder()
                .itemId(newItemDto.getId())
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusSeconds(3))
                .build();

        BookingDto bookingDto = bookingService.addBooking(bookingInputDto, newUserDto.getId());

        BookingDto updatedBookingDto = bookingService.updateBooking(bookingDto.getId(),
                newUserDto.getId(), false);

        assertEquals(bookingDto.getId(), updatedBookingDto.getId());
        assertEquals(Status.CANCELED, updatedBookingDto.getStatus());
    }

    @Test
    void shouldExceptionWhenBookerApprovedBooking() {
        UserDto ownerDto = userService.addUser(userDto1);
        ItemDto newItemDto = itemService.addItem(itemDto1, ownerDto.getId());
        UserDto newUserDto = userService.addUser(userDto2);
        BookingInputDto bookingInputDto = BookingInputDto
                .builder()
                .itemId(newItemDto.getId())
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusSeconds(3))
                .build();

        BookingDto bookingDto = bookingService.addBooking(bookingInputDto, newUserDto.getId());

        assertThrows(PermissionException.class,
                () -> bookingService.updateBooking(bookingDto.getId(),
                        newUserDto.getId(), true));
    }

    @Test
    void shouldExceptionWhenUpdateApprovedBooking() {
        UserDto ownerDto = userService.addUser(userDto1);
        ItemDto newItemDto = itemService.addItem(itemDto1, ownerDto.getId());
        UserDto newUserDto = userService.addUser(userDto2);
        BookingInputDto bookingInputDto = BookingInputDto
                .builder()
                .itemId(newItemDto.getId())
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusSeconds(3))
                .build();

        BookingDto bookingDto = bookingService.addBooking(bookingInputDto, newUserDto.getId());
        BookingDto updatedBooking  = bookingService.updateBooking(bookingDto.getId(), ownerDto.getId(), true);
        System.out.println(updatedBooking.getStatus());
        ValidationException exp = assertThrows(ValidationException.class,
                () -> bookingService.updateBooking(bookingDto.getId(),
                        ownerDto.getId(), false));
        assertEquals("Решение по бронированию уже есть", exp.getMessage());
    }

    @Test
    void shouldSetRejectedStatusWhenUpdate() {
        UserDto ownerDto = userService.addUser(userDto1);
        ItemDto newItemDto = itemService.addItem(itemDto1, ownerDto.getId());
        UserDto newUserDto = userService.addUser(userDto2);
        BookingInputDto bookingInputDto = BookingInputDto
                .builder()
                .itemId(newItemDto.getId())
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusSeconds(3))
                .build();

        BookingDto bookingDto = bookingService.addBooking(bookingInputDto, newUserDto.getId());
        BookingDto updatedBookingDto = bookingService.updateBooking(bookingDto.getId(),
                ownerDto.getId(), false);
        assertEquals(bookingDto.getId(), updatedBookingDto.getId());
        assertEquals(Status.REJECTED, updatedBookingDto.getStatus());
    }

    @Test
    void shouldExceptionWhenApprovedAndCanceled() {
        UserDto ownerDto = userService.addUser(userDto1);
        ItemDto newItemDto = itemService.addItem(itemDto1, ownerDto.getId());
        UserDto newUserDto = userService.addUser(userDto2);
        BookingInputDto bookingInputDto = BookingInputDto
                .builder()
                .itemId(newItemDto.getId())
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusSeconds(3))
                .build();

        BookingDto bookingDto = bookingService.addBooking(bookingInputDto, newUserDto.getId());
        bookingService.updateBooking(bookingDto.getId(), newUserDto.getId(), false);

        ValidationException exp = assertThrows(ValidationException.class,
                () -> bookingService.updateBooking(bookingDto.getId(),
                        ownerDto.getId(), true));

        assertEquals("Бронирование было отменено!", exp.getMessage());
    }

    @Test
    void shouldExceptionWhenBookerTryApprove() {
        UserDto ownerDto = userService.addUser(userDto1);
        ItemDto newItemDto = itemService.addItem(itemDto1, ownerDto.getId());
        UserDto newUserDto = userService.addUser(userDto2);
        UserDto userDto3 = UserDto
                .builder()
                .email("Email@email.ru")
                .name("name")
                .build();
        UserDto newUserDto1 = userService.addUser(userDto3);

        BookingInputDto bookingInputDto = BookingInputDto
                .builder()
                .itemId(newItemDto.getId())
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusSeconds(3))
                .build();

        BookingDto bookingDto = bookingService.addBooking(bookingInputDto, newUserDto.getId());

        ValidationException exp = assertThrows(ValidationException.class,
                () -> bookingService.updateBooking(bookingDto.getId(),
                        newUserDto1.getId(), true));

        assertEquals("Подтвердить бронирование может только владелец вещи!", exp.getMessage());
    }

    @Test
    void shouldReturnCurrentBookings() {
        UserDto ownerDto = userService.addUser(userDto1);
        ItemDto newItemDto = itemService.addItem(itemDto1, ownerDto.getId());
        UserDto newUserDto = userService.addUser(userDto2);
        BookingInputDto bookingInputDto = BookingInputDto
                .builder()
                .itemId(newItemDto.getId())
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusSeconds(10))
                .build();
        BookingDto bookingDto = bookingService.addBooking(bookingInputDto, newUserDto.getId());
        List<BookingDto> result = bookingService.getBookings("CURRENT", newUserDto.getId(), 0, 10);
        assertEquals(1, result.size());
        assertEquals(bookingDto.getId(), result.get(0).getId());
    }

    @Test
    void shouldReturnPastBooking() {
        UserDto ownerDto = userService.addUser(userDto1);
        ItemDto newItemDto = itemService.addItem(itemDto1, ownerDto.getId());
        UserDto newUserDto = userService.addUser(userDto2);
        BookingInputDto bookingInputDto = BookingInputDto
                .builder()
                .itemId(newItemDto.getId())
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusSeconds(1))
                .build();
        BookingDto bookingDto = bookingService.addBooking(bookingInputDto, newUserDto.getId());
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            return;
        }
        List<BookingDto> result = bookingService.getBookings("PAST", newUserDto.getId(), 0, 10);
        assertEquals(1, result.size());
        assertEquals(bookingDto.getId(), result.get(0).getId());
    }

    @Test
    void shouldReturnFutureBookings() {
        UserDto ownerDto = userService.addUser(userDto1);
        ItemDto newItemDto = itemService.addItem(itemDto1, ownerDto.getId());
        UserDto newUserDto = userService.addUser(userDto2);
        BookingInputDto bookingInputDto = BookingInputDto
                .builder()
                .itemId(newItemDto.getId())
                .start(LocalDateTime.now().plusSeconds(10))
                .end(LocalDateTime.now().plusSeconds(11))
                .build();
        BookingDto bookingDto = bookingService.addBooking(bookingInputDto, newUserDto.getId());
        List<BookingDto> result = bookingService.getBookings("FUTURE", newUserDto.getId(), 0, 10);
        assertEquals(1, result.size());
        assertEquals(bookingDto.getId(), result.get(0).getId());
    }

    @Test
    void shouldExceptionWhenUnknownState() {
        UserDto ownerDto = userService.addUser(userDto1);
        ItemDto newItemDto = itemService.addItem(itemDto1, ownerDto.getId());
        UserDto newUserDto = userService.addUser(userDto2);
        BookingInputDto bookingInputDto = BookingInputDto
                .builder()
                .itemId(newItemDto.getId())
                .start(LocalDateTime.now().plusSeconds(10))
                .end(LocalDateTime.now().plusSeconds(11))
                .build();
        bookingService.addBooking(bookingInputDto, newUserDto.getId());
        assertThrows(ValidationException.class,
                () -> bookingService.getBookings("-_-", newUserDto.getId(), 0, 10));
    }

    @Test
    void shouldExceptionWhenStartEqualsEnd() {
        UserDto ownerDto = userService.addUser(userDto1);
        ItemDto newItemDto = itemService.addItem(itemDto1, ownerDto.getId());
        UserDto newUserDto = userService.addUser(userDto2);
        LocalDateTime localDateTime = LocalDateTime.now().plusSeconds(10);
        BookingInputDto bookingInputDto = BookingInputDto
                .builder()
                .itemId(newItemDto.getId())
                .start(localDateTime)
                .end(localDateTime)
                .build();
        assertThrows(ValidationException.class,
                () -> bookingService.addBooking(bookingInputDto, newUserDto.getId()));
    }

    @Test
    void shouldReturnBooking() {
        UserDto ownerDto = userService.addUser(userDto1);
        ItemDto newItemDto = itemService.addItem(itemDto1, ownerDto.getId());
        UserDto newUserDto = userService.addUser(userDto2);
        BookingInputDto bookingInputDto = BookingInputDto
                .builder()
                .itemId(newItemDto.getId())
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusSeconds(1))
                .build();
        BookingDto bookingDto = bookingService.addBooking(bookingInputDto, newUserDto.getId());

        BookingDto savedBooking = bookingService.getBookingById(
                bookingDto.getId(),
                newUserDto.getId()
        );

        assertEquals(bookingDto.getId(), savedBooking.getId());
    }

    @Test
    void shouldReturnCurrentWhenGetByOwner() {
        UserDto ownerDto = userService.addUser(userDto1);
        ItemDto newItemDto = itemService.addItem(itemDto1, ownerDto.getId());
        UserDto newUserDto = userService.addUser(userDto2);
        BookingInputDto bookingInputDto = BookingInputDto
                .builder()
                .itemId(newItemDto.getId())
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusSeconds(20))
                .build();

        BookingDto bookingDto = bookingService.addBooking(
                bookingInputDto,
                newUserDto.getId()
        );

        List<BookingDto> bookings = bookingService.getBookingsOwner(
                "CURRENT",
                ownerDto.getId(),
                0,
                10
        );

        assertEquals(1, bookings.size());
        assertEquals(bookingDto.getId(), bookings.get(0).getId());
    }

    @Test
    void shouldReturnPastWhenGetByOwner() {
        UserDto ownerDto = userService.addUser(userDto1);
        ItemDto newItemDto = itemService.addItem(itemDto1, ownerDto.getId());
        UserDto newUserDto = userService.addUser(userDto2);
        BookingInputDto bookingInputDto = BookingInputDto
                .builder()
                .itemId(newItemDto.getId())
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusSeconds(1))
                .build();

        BookingDto bookingDto = bookingService.addBooking(
                bookingInputDto,
                newUserDto.getId()
        );
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            return;
        }

        List<BookingDto> bookings = bookingService.getBookingsOwner(
                "PAST",
                ownerDto.getId(),
                0,
                10
        );

        assertEquals(1, bookings.size());
        assertEquals(bookingDto.getId(), bookings.get(0).getId());
    }

    @Test
    void shouldReturnFutureWhenGetByOwner() {
        UserDto ownerDto = userService.addUser(userDto1);
        ItemDto newItemDto = itemService.addItem(itemDto1, ownerDto.getId());
        UserDto newUserDto = userService.addUser(userDto2);
        BookingInputDto bookingInputDto = BookingInputDto
                .builder()
                .itemId(newItemDto.getId())
                .start(LocalDateTime.now().plusSeconds(10))
                .end(LocalDateTime.now().plusSeconds(20))
                .build();

        BookingDto bookingDto = bookingService.addBooking(
                bookingInputDto,
                newUserDto.getId()
        );

        List<BookingDto> bookings = bookingService.getBookingsOwner(
                "FUTURE",
                ownerDto.getId(),
                0,
                10
        );

        assertEquals(1, bookings.size());
        assertEquals(bookingDto.getId(), bookings.get(0).getId());
    }

    @Test
    void shouldExceptionWhenUnknownStateWhenGetByOwner() {
        UserDto ownerDto = userService.addUser(userDto1);
        assertThrows(ValidationException.class,
                () -> bookingService.getBookingsOwner(
                        "-_-",
                        ownerDto.getId(),
                        0,
                        10
                ));
    }

    @Test
    void shouldExceptionWhenAddBookingAndNotFoundUser() {
        UserDto ownerDto = userService.addUser(userDto1);
        ItemDto newItemDto = itemService.addItem(itemDto1, ownerDto.getId());
        BookingInputDto bookingInputDto = BookingInputDto
                .builder()
                .itemId(newItemDto.getId())
                .start(LocalDateTime.now().plusSeconds(10))
                .end(LocalDateTime.now().plusSeconds(20))
                .build();

        assertThrows(NotFoundException.class,
                () -> bookingService.addBooking(bookingInputDto, 666L));
    }

    @Test
    void shouldExceptionWhenAddBookingAndItemNotAvailable() {
        UserDto ownerDto = userService.addUser(userDto1);
        itemDto1.setAvailable(false);
        ItemDto newItemDto = itemService.addItem(itemDto1, ownerDto.getId());
        itemDto1.setAvailable(true);
        UserDto newUserDto = userService.addUser(userDto2);
        BookingInputDto bookingInputDto = BookingInputDto
                .builder()
                .itemId(newItemDto.getId())
                .start(LocalDateTime.now().plusSeconds(10))
                .end(LocalDateTime.now().plusSeconds(20))
                .build();

        assertThrows(ValidationException.class,
                () -> bookingService.addBooking(bookingInputDto, newUserDto.getId()));
    }

    @Test
    void shouldExceptionWhenAddBookingAndItemNotExist() {
        UserDto ownerDto = userService.addUser(userDto1);
        UserDto newUserDto = userService.addUser(userDto2);
        BookingInputDto bookingInputDto = BookingInputDto
                .builder()
                .itemId(666L)
                .start(LocalDateTime.now().plusSeconds(10))
                .end(LocalDateTime.now().plusSeconds(20))
                .build();

        assertThrows(NotFoundException.class,
                () -> bookingService.addBooking(bookingInputDto, newUserDto.getId()));
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
        assertThrows(NotFoundException.class,
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