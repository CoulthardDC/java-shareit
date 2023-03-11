package ru.practicum.shareit.booking;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingShortDto;

import java.time.LocalDateTime;

@JsonTest
public class BookingShortDtoTest {

    @Autowired
    private JacksonTester<BookingShortDto> json;

    @Test
    public void testBookingShortDto() throws Exception{
        BookingShortDto bookingShortDto = BookingShortDto
                .builder()
                .id(1L)
                .bookerId(2L)
                .start(LocalDateTime.of(2023, 3, 11, 12, 0))
                .end(LocalDateTime.of(2023, 3, 12, 12, 0))
                .build();

        JsonContent<BookingShortDto> result = json.write(bookingShortDto);

        Assertions.assertThat(result)
                .extractingJsonPathNumberValue("$.id").isEqualTo(1);
        Assertions.assertThat(result)
                .extractingJsonPathNumberValue("$.bookerId").isEqualTo(2);
        Assertions.assertThat(result)
                .extractingJsonPathStringValue("$.start").isEqualTo("2023-03-11T12:00:00");
        Assertions.assertThat(result)
                .extractingJsonPathStringValue("$.end").isEqualTo("2023-03-12T12:00:00");
    }
}
