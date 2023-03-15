package ru.practicum.shareit.booking;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingInputDto;

import java.time.LocalDateTime;

@JsonTest
public class BookingInputDtoTest {
    BookingInputDto bookingInputDto = BookingInputDto
            .builder()
            .itemId(1L)
            .start(LocalDateTime.of(2023, 3, 11, 12, 0))
            .end(LocalDateTime.of(2023, 3, 12, 12, 0))
            .build();

    @Autowired
    private JacksonTester<BookingInputDto> json;

    @Test
    public void testBookingInputDto() throws Exception {
        JsonContent<BookingInputDto> result = json.write(bookingInputDto);

        Assertions.assertThat(result)
                .extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        Assertions.assertThat(result)
                .extractingJsonPathStringValue("$.start").isEqualTo("2023-03-11T12:00:00");
        Assertions.assertThat(result)
                .extractingJsonPathStringValue("$.end").isEqualTo("2023-03-12T12:00:00");
    }

    @Test
    public void testBookingInputSetTest() {
        LocalDateTime localDateTimeStart = bookingInputDto.getStart();
        LocalDateTime localDateTimeEnd = bookingInputDto.getEnd();

        bookingInputDto.setItemId(1L);
        bookingInputDto.setStart(localDateTimeStart);
        bookingInputDto.setEnd(localDateTimeEnd);

        org.junit.jupiter.api.Assertions.assertEquals(1L, bookingInputDto.getItemId());
        org.junit.jupiter.api.Assertions.assertEquals(localDateTimeStart, bookingInputDto.getStart());
        org.junit.jupiter.api.Assertions.assertEquals(localDateTimeEnd, bookingInputDto.getEnd());
    }
}
