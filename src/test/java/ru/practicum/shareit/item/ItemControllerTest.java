package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.CommentCreateException;
import ru.practicum.shareit.item.exception.ItemCreateException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@WebMvcTest(controllers = {ItemController.class})
public class ItemControllerTest {
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemService itemService;

    @Autowired
    private MockMvc mvc;

    private static final String X_HEADER = "X-Sharer-User-Id";

    private final List<ItemDto> listItemDto = new ArrayList<>();

    private final UserDto userOwnerDto = UserDto
            .builder()
            .id(1L)
            .email("example@example.com")
            .name("userName")
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

    private final CommentDto commentDto = CommentDto
            .builder()
            .id(1L)
            .text("text")
            .itemId(1L)
            .authorName("userName")
            .created(LocalDateTime.of(2023, 4, 11, 12, 0))
            .build();

    @Test
    public void testCreateItem() throws Exception {
        Mockito.when(itemService.addItem(Mockito.any(), Mockito.anyLong()))
                .thenReturn(itemDto);

        mvc.perform(post("/items")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .header(X_HEADER, 1)
                .content(mapper.writeValueAsString(itemDto)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                    .andExpect(jsonPath("$.name", is(itemDto.getName()), String.class))
                    .andExpect(jsonPath("$.description", is(itemDto.getDescription()), String.class))
                    .andExpect(jsonPath("$.owner.id", is(itemDto.getOwner().getId()), Long.class))
                    .andExpect(jsonPath("$.available", is(itemDto.getAvailable()), Boolean.class));
    }

    @Test
    public void testUpdateItem() throws Exception {
        Mockito.when(itemService.updateItem(Mockito.any(), Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(itemDto);

        mvc.perform(patch("/items/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(X_HEADER, 1)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(mapper.writeValueAsString(itemDto)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                    .andExpect(jsonPath("$.name", is(itemDto.getName()), String.class))
                    .andExpect(jsonPath("$.description", is(itemDto.getDescription()), String.class))
                    .andExpect(jsonPath("$.owner.id", is(itemDto.getOwner().getId()), Long.class))
                    .andExpect(jsonPath("$.available", is(itemDto.getAvailable()), Boolean.class));
    }

    @Test
    public void testFindItemById() throws Exception {
        Mockito.when(itemService.getItemById(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(itemDto);

        mvc.perform(get("/items/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(X_HEADER, 1)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(mapper.writeValueAsString(itemDto)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                    .andExpect(jsonPath("$.name", is(itemDto.getName()), String.class))
                    .andExpect(jsonPath("$.description", is(itemDto.getDescription()), String.class))
                    .andExpect(jsonPath("$.owner.id", is(itemDto.getOwner().getId()), Long.class))
                    .andExpect(jsonPath("$.available", is(itemDto.getAvailable()), Boolean.class));
    }

    @Test void testFindItemsByOwner() throws Exception {
        Mockito.when(itemService.getItemsByOwner(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(List.of(itemDto));

        mvc.perform(get("/items")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(X_HEADER, 1)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(mapper.writeValueAsString(listItemDto)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.[0].id", is(itemDto.getId()), Long.class))
                    .andExpect(jsonPath("$.[0].name", is(itemDto.getName()), String.class))
                    .andExpect(jsonPath("$.[0].description", is(itemDto.getDescription()), String.class))
                    .andExpect(jsonPath("$.[0].owner.id", is(itemDto.getOwner().getId()), Long.class))
                    .andExpect(jsonPath("$.[0].available", is(itemDto.getAvailable()), Boolean.class));
    }

    @Test
    public void testSearchItems() throws Exception {
        Mockito.when(itemService.getItemsBySearch(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(List.of(itemDto));

        mvc.perform(get("/items/search")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(X_HEADER, 1)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(mapper.writeValueAsString(listItemDto)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.[0].id", is(itemDto.getId()), Long.class))
                    .andExpect(jsonPath("$.[0].name", is(itemDto.getName()), String.class))
                    .andExpect(jsonPath("$.[0].description", is(itemDto.getDescription()), String.class))
                    .andExpect(jsonPath("$.[0].owner.id", is(itemDto.getOwner().getId()), Long.class))
                    .andExpect(jsonPath("$.[0].available", is(itemDto.getAvailable()), Boolean.class));
    }

    @Test
    public void testAddComment() throws Exception {
        Mockito.when(itemService.addComment(Mockito.any(), Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(commentDto);

        mvc.perform(post("/items/1/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(X_HEADER, 1)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(mapper.writeValueAsString(commentDto)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id", is(commentDto.getItemId()), Long.class))
                    .andExpect(jsonPath("$.text", is(commentDto.getText()), String.class))
                    .andExpect(jsonPath("$.itemId", is(commentDto.getItemId()), Long.class))
                    .andExpect(jsonPath("$.authorName", is(commentDto.getAuthorName()), String.class))
                    .andExpect(jsonPath("$.created",
                            is(commentDto.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))));
    }

    @Test
    public void testHandleItemNotFoundException() throws Exception {
        Mockito.when(itemService.getItemById(Mockito.anyLong(), Mockito.anyLong()))
                .thenThrow(ItemNotFoundException.class);

        mvc.perform(get("/items/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(X_HEADER, 1)
                .content(mapper.writeValueAsString(itemDto))
                .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("Вещь не найдена"), String.class));
    }

    @Test
    public void testHandleItemCreateException() throws Exception {
        Mockito.when(itemService.addItem(Mockito.any(), Mockito.anyLong()))
                .thenThrow(ItemCreateException.class);

        mvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(X_HEADER, 1)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("Ошибка при создании вещи"), String.class));
    }

    @Test
    public void testHandleMissingRequestHeaderException() throws Exception {
        mvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Отсутствует необходимы заголовок"), String.class));
    }

    @Test
    public void testHandleCommentCreateException() throws Exception {
        Mockito.when(itemService.addComment(Mockito.any(), Mockito.anyLong(), Mockito.anyLong()))
                .thenThrow(CommentCreateException.class);

        mvc.perform(post("/items/1/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(X_HEADER, 1)
                        .content(mapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Ошибка при создании коментария"), String.class));
    }
}
