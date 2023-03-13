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

@JsonTest
public class BookingDtoTest {

    @Autowired
    private JacksonTester<BookingDto> json;

    @Test
    void testBookingDto() throws Exception {
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
}
