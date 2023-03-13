package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.exception.PermissionException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.ValidationException;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@WebMvcTest(controllers = {BookingController.class})
public class BookingControllerTest {
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private MockMvc mvc;

    private static final String X_HEADER = "X-Sharer-User-Id";

    private final List<BookingDto> listBookingDto = new ArrayList<>();

    private final BookingInputDto bookingInputDto = BookingInputDto
            .builder()
            .itemId(1L)
            .start(LocalDateTime.of(2023, 4, 11, 12, 0))
            .end(LocalDateTime.of(2023, 4, 12, 12, 0))
            .build();

    private final UserDto userOwnerDto = UserDto
            .builder()
            .id(1L)
            .email("example@example.com")
            .name("userName")
            .build();

    private final UserDto userBookerDto = UserDto
            .builder()
            .id(2L)
            .email("example2@example.com")
            .name("userName2")
            .build();

    private final ItemDto itemDto = ItemDto
            .builder()
            .id(1L)
            .name("ItemName")
            .description("description")
            .owner(userOwnerDto)
            .available(true)
            .requestId(null)
            .comments(null)
            .lastBooking(null)
            .nextBooking(null)
            .build();
    private final BookingDto bookingDto = BookingDto
            .builder()
            .id(1L)
            .start(LocalDateTime.of(2023, 4, 11, 12, 0))
            .end(LocalDateTime.of(2023, 4, 12, 12, 0))
            .item(itemDto)
            .booker(userBookerDto)
            .status(Status.WAITING)
            .build();


    @Test
    public void testAddBooking() throws Exception {
        Mockito.when(bookingService.addBooking(Mockito.any(), Mockito.anyLong()))
                .thenReturn(bookingDto);

        mvc.perform(post("/bookings")
                .header(X_HEADER, 2)
                .content(mapper.writeValueAsString(bookingInputDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id ", is(bookingDto.getId()), Long.class))
                    .andExpect(jsonPath("$.start",
                            is(bookingDto.getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                    .andExpect(jsonPath("$.end",
                            is(bookingDto.getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                    .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString()), Status.class));
    }

    @Test
    public void testFindBookings() throws Exception {
        Mockito.when(bookingService.getBookings(Mockito.anyString(),
                Mockito.anyLong(),
                Mockito.anyInt(),
                Mockito.anyInt())).thenReturn(List.of(bookingDto));

        mvc.perform(get("/bookings")
                .header(X_HEADER, 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(listBookingDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.[0].id", is(bookingDto.getId()), Long.class))
                    .andExpect(jsonPath("$.[0].start",
                            is(bookingDto.getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                    .andExpect(jsonPath("$.[0].end",
                            is(bookingDto.getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                    .andExpect(jsonPath("$.[0].status",
                            is(bookingDto.getStatus().toString()), String.class));
    }

    @Test
    public void testFindBookingById() throws Exception {
        Mockito.when(bookingService.getBookingById(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(bookingDto);

        mvc.perform(get("/bookings/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(X_HEADER, 2)
                .content(mapper.writeValueAsString(bookingDto))
                .characterEncoding(StandardCharsets.UTF_8))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                    .andExpect(jsonPath("$.start",
                            is(bookingDto.getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                    .andExpect(jsonPath("$.end",
                            is(bookingDto.getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                    .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString()), String.class));
    }

    @Test
    public void testFindBookingsOwner() throws Exception {
        Mockito.when(bookingService.getBookingsOwner(Mockito.anyString(),
                Mockito.anyLong(),
                Mockito.anyInt(),
                Mockito.anyInt())).thenReturn(List.of(bookingDto));

        mvc.perform(get("/bookings/owner")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(X_HEADER, 2)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(mapper.writeValueAsString(listBookingDto)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.[0].id", is(bookingDto.getId()), Long.class))
                    .andExpect(jsonPath("$.[0].start",
                            is(bookingDto.getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                    .andExpect(jsonPath("$.[0].end",
                            is(bookingDto.getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                    .andExpect(jsonPath("$.[0].status", is(bookingDto.getStatus().toString()), String.class))
                    .andExpect(jsonPath("$.[0].item.id", is(bookingDto.getItem().getId()), Long.class))
                    .andExpect(jsonPath("$.[0].booker.id",
                            is(bookingDto.getBooker().getId()), Long.class));
    }

    @Test
    public void testUpdate() throws Exception {
        Mockito.when(bookingService.updateBooking(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyBoolean()))
                .thenReturn(bookingDto);

        mvc.perform(patch("/bookings/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(X_HEADER, 2)
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .queryParam("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.start",
                        is(bookingDto.getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.end",
                        is(bookingDto.getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString()), String.class));
    }

    @Test
    public void testHandleBookingNotFoundException() throws Exception {
        Mockito.when(bookingService.getBookingById(Mockito.anyLong(), Mockito.anyLong()))
                .thenThrow(BookingNotFoundException.class);

        mvc.perform(get("/bookings/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(X_HEADER, 2)
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testHandleValidationException() throws Exception {
        Mockito.when(bookingService.getBookingById(Mockito.anyLong(), Mockito.anyLong()))
                .thenThrow(ValidationException.class);

        mvc.perform(get("/bookings/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(X_HEADER, 2)
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testHandlePermissionException() throws Exception {
        Mockito.when(bookingService.getBookingById(Mockito.anyLong(), Mockito.anyLong()))
                .thenThrow(PermissionException.class);

        mvc.perform(get("/bookings/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(X_HEADER, 2)
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("Ошибка доступа"), String.class));
    }
}
