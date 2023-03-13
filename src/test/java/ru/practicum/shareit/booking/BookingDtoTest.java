package ru.practicum.shareit.booking;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.Objects;

@JsonTest
public class BookingDtoTest {

    @Autowired
    private JacksonTester<BookingDto> json;

    UserDto userOwnerDto = UserDto
            .builder()
            .id(1L)
            .email("example@example.ru")
            .name("name")
            .build();

    UserDto userBookerDto = UserDto
            .builder()
            .id(2L)
            .email("example2@example.ru")
            .name("name2")
            .build();

    ItemDto itemDto = ItemDto
            .builder()
            .id(1L)
            .name("item name")
            .description("item description")
            .owner(userOwnerDto)
            .available(true)
            .build();

    BookingDto bookingDto = BookingDto
            .builder()
            .id(1L)
            .start(LocalDateTime.of(2023, 3, 11, 12, 0))
            .end(LocalDateTime.of(2023, 3, 12, 12, 0))
            .item(itemDto)
            .booker(userBookerDto)
            .status(Status.WAITING)
            .build();

    @Test
    void testBookingDto() throws Exception {

        JsonContent<BookingDto> result = json.write(bookingDto);

        Assertions.assertThat(result)
                .extractingJsonPathNumberValue("$.id").isEqualTo(1);
        Assertions.assertThat(result)
                .extractingJsonPathStringValue("$.start").isEqualTo("2023-03-11T12:00:00");
        Assertions.assertThat(result)
                .extractingJsonPathStringValue("$.end").isEqualTo("2023-03-12T12:00:00");
        Assertions.assertThat(result)
                .extractingJsonPathStringValue("$.status").isEqualTo("WAITING");

    }

    @Test
    public void testBookingDtoSetId() {
        Long id = 666L;
        bookingDto.setId(id);
        org.junit.jupiter.api.Assertions.assertEquals(id, bookingDto.getId());
        bookingDto.setId(1L);
    }

    @Test
    public void testBookingDtoSetStartAndEnd() {
        LocalDateTime prevStart = bookingDto.getStart();
        LocalDateTime prevEnd = bookingDto.getEnd();
        LocalDateTime newDate = LocalDateTime.of(666, 12, 12, 12, 12, 12);

        bookingDto.setStart(newDate);
        bookingDto.setEnd(newDate);
        bookingDto.setItem(itemDto);

        org.junit.jupiter.api.Assertions.assertEquals(newDate, bookingDto.getStart());
        org.junit.jupiter.api.Assertions.assertEquals(newDate, bookingDto.getEnd());

        bookingDto.setStart(prevStart);
        bookingDto.setEnd(prevEnd);
    }

    @Test
    public void testBookingDtoEqualsAndHashCode() {
        BookingDto newBookingDto = BookingDto
                .builder()
                .id(1L)
                .start(LocalDateTime.of(2023, 3, 11, 12, 0))
                .end(LocalDateTime.of(2023, 3, 12, 12, 0))
                .item(itemDto)
                .booker(userBookerDto)
                .status(Status.WAITING)
                .build();

        org.junit.jupiter.api.Assertions.assertTrue(bookingDto.equals(bookingDto));
        org.junit.jupiter.api.Assertions.assertFalse(bookingDto.equals(null));
        org.junit.jupiter.api.Assertions.assertTrue(bookingDto.equals(newBookingDto));
        org.junit.jupiter.api.Assertions.assertEquals(Objects.hash(bookingDto.getId()), bookingDto.hashCode());
    }
}
