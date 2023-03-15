package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@WebMvcTest(controllers = {ItemRequestController.class})
public class ItemRequestControllerTest {
    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemRequestService itemRequestService;

    @Autowired
    MockMvc mvc;

    private static final String X_HEADER = "X-Sharer-User-Id";

    private final List<ItemRequestDto> listItemRequestDto = new ArrayList<>();

    private final UserDto userDto = UserDto
            .builder()
            .id(1L)
            .email("example@Example.com")
            .name("name")
            .build();

    private final ItemRequestDto itemRequestDto = ItemRequestDto
            .builder()
            .id(1L)
            .description("description")
            .requestor(userDto)
            .created(LocalDateTime.of(2023, 5, 11, 12, 0))
            .build();

    @Test
    public void testCreateItemRequest() throws Exception {
        Mockito.when(itemRequestService.addRequest(Mockito.any(), Mockito.anyLong(), Mockito.any()))
                .thenReturn(itemRequestDto);

        mvc.perform(post("/requests")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(X_HEADER, 1)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(mapper.writeValueAsString(itemRequestDto)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id", is(itemRequestDto.getId()), Long.class))
                    .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription()), String.class))
                    .andExpect(jsonPath("$.requestor.id", is(itemRequestDto.getRequestor().getId()), Long.class))
                    .andExpect(jsonPath("$.created",
                            is(itemRequestDto.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))));
    }

    @Test
    public void testGetAllUsersRequests() throws Exception {
        Mockito.when(itemRequestService.getAllRequestsByUser(Mockito.anyLong()))
                .thenReturn(List.of(itemRequestDto));

        mvc.perform(get("/requests")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(X_HEADER, 1)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(mapper.writeValueAsString(listItemRequestDto)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.[0].id", is(itemRequestDto.getId()), Long.class))
                    .andExpect(jsonPath("$.[0].description", is(itemRequestDto.getDescription()), String.class))
                    .andExpect(jsonPath("$.[0].requestor.id", is(itemRequestDto.getRequestor().getId()), Long.class))
                    .andExpect(jsonPath("$.[0].created",
                        is(itemRequestDto.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))));
    }

    @Test
    public void testGetAllRequests() throws Exception {
        Mockito.when(itemRequestService.getAllRequests(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(List.of(itemRequestDto));

        mvc.perform(get("/requests/all")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(X_HEADER, 1)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(mapper.writeValueAsString(listItemRequestDto)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.[0].id", is(itemRequestDto.getId()), Long.class))
                    .andExpect(jsonPath("$.[0].description", is(itemRequestDto.getDescription()), String.class))
                    .andExpect(jsonPath("$.[0].requestor.id", is(itemRequestDto.getRequestor().getId()), Long.class))
                    .andExpect(jsonPath("$.[0].created",
                        is(itemRequestDto.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))));
    }

    @Test
    public void testGetRequestById() throws Exception {
        Mockito.when(itemRequestService.getRequestById(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(itemRequestDto);

        mvc.perform(get("/requests/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(X_HEADER, 1)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(mapper.writeValueAsString(itemRequestDto)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id", is(itemRequestDto.getId()), Long.class))
                    .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription()), String.class))
                    .andExpect(jsonPath("$.requestor.id", is(itemRequestDto.getRequestor().getId()), Long.class))
                    .andExpect(jsonPath("$.created",
                        is(itemRequestDto.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))));
    }

    @Test
    public void testHandleItemRequestNotFoundException() throws Exception {
        Mockito.when(itemRequestService.getRequestById(Mockito.anyLong(), Mockito.anyLong()))
                .thenThrow(ItemRequestNotFoundException.class);

        mvc.perform(get("/requests/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(X_HEADER, 1)
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Реквест не найден"), String.class));
    }
}
