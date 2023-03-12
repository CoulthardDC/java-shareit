package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.item.exception.CommentCreateException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.PermissionException;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static java.lang.Thread.sleep;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceTest {
    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingService;
    private final UserDto userDto = UserDto
            .builder()
            .id(200L)
            .name("first")
            .email("first@first300.ru")
            .build();
    private final UserDto userDto1 = UserDto
            .builder()
            .id(201L)
            .email("example1@example.com")
            .name("name1")
            .build();
    private final UserDto userDto2 = UserDto
            .builder()
            .id(202L)
            .email("example2@example.com")
            .name("name2")
            .build();

    private final ItemDto itemDto = ItemDto
            .builder()
            .id(200L)
            .name("item1")
            .description("description1")
            .owner(userDto)
            .available(true)
            .build();

    private final ItemDto itemDto2 = ItemDto
            .builder()
            .id(202L)
            .name("item1")
            .description("description1")
            .owner(userDto)
            .available(true)
            .build();

    @Test
    void shouldCreateItem() {
        UserDto newUserDto = userService.addUser(userDto1);
        ItemDto newItemDto = itemService.addItem(itemDto, newUserDto.getId());
        ItemDto returnItemDto = itemService.getItemById(newItemDto.getId(), newUserDto.getId());
        assertThat(returnItemDto.getDescription(), equalTo(itemDto.getDescription()));
    }

    @Test
    void shouldDeleteItemWhenUserNotOwner() {
        UserDto ownerDto = userService.addUser(userDto1);
        UserDto newUserDto = userService.addUser(userDto2);
        ItemDto newItemDto = itemService.addItem(itemDto, ownerDto.getId());
        PermissionException exp = assertThrows(PermissionException.class,
                () -> itemService.removeItem(newItemDto.getId(), newUserDto.getId()));
    }

    @Test
    void shouldDeleteWhenUserIsOwner() {
        UserDto ownerDto = userService.addUser(userDto1);
        ItemDto newItemDto = itemService.addItem(itemDto, ownerDto.getId());
        itemService.removeItem(newItemDto.getId(), ownerDto.getId());
        ItemNotFoundException exp = assertThrows(ItemNotFoundException.class,
                () -> itemService.getItemById(newItemDto.getId(), ownerDto.getId()));
    }

    @Test
    void shouldExceptionWhenDeleteItemNotExist() {
        UserDto ownerDto = userService.addUser(userDto1);
        ItemNotFoundException exp = assertThrows(ItemNotFoundException.class,
                () -> itemService.removeItem(-2L, ownerDto.getId()));
    }

    @Test
    void shouldUpdateItem() {
        UserDto newUserDto = userService.addUser(userDto1);
        ItemDto newItemDto = itemService.addItem(itemDto, newUserDto.getId());
        newItemDto.setName("NewName");
        newItemDto.setDescription("NewDescription");
        newItemDto.setAvailable(false);
        ItemDto returnItemDto = itemService.updateItem(newItemDto, newItemDto.getId(), newUserDto.getId());
        assertThat(returnItemDto.getName(), equalTo("NewName"));
        assertThat(returnItemDto.getDescription(), equalTo("NewDescription"));
        assertFalse(returnItemDto.getAvailable());
    }

    @Test
    void shouldExceptionWhenUpdateItemNotOwner() {
        UserDto ownerDto = userService.addUser(userDto1);
        UserDto newUserDto = userService.addUser(userDto2);
        ItemDto newItemDto = itemService.addItem(itemDto, ownerDto.getId());
        ItemNotFoundException exp = assertThrows(ItemNotFoundException.class,
                () -> itemService.updateItem(newItemDto, newItemDto.getId(), newUserDto.getId()));
    }

    @Test
    void shouldReturnItemsByOwner() {
        UserDto ownerDto = userService.addUser(userDto1);
        itemService.addItem(itemDto, ownerDto.getId());
        itemService.addItem(itemDto2, ownerDto.getId());
        List<ItemDto> listItems = itemService.getItemsByOwner(ownerDto.getId(), 0, 10);
        assertEquals(2, listItems.size());
    }

    @Test
    void shouldReturnItemsBySearch() {
        UserDto ownerDto = userService.addUser(userDto1);
        itemService.addItem(itemDto, ownerDto.getId());
        itemService.addItem(itemDto2, ownerDto.getId());
        List<ItemDto> listItems = itemService.getItemsBySearch("item", 0, 1);
        assertEquals(1, listItems.size());
    }

    @Test
    void shouldExceptionWhenCreateCommentWhenUserNotBooker() {
        UserDto ownerDto = userService.addUser(userDto1);
        UserDto newUserDto = userService.addUser(userDto2);
        ItemDto newItemDto = itemService.addItem(itemDto, ownerDto.getId());
        CommentDto commentDto = CommentDto
                .builder()
                .id(1L)
                .text("Comment1")
                .itemId(itemDto.getId())
                .authorName(newUserDto.getName())
                .created(LocalDateTime.now())
                .build();
        CommentCreateException exp = assertThrows(CommentCreateException.class,
                () -> itemService.addComment(commentDto, newItemDto.getId(), newUserDto.getId()));
    }

    @Test
    void shouldCreateComment() {
        UserDto ownerDto = userService.addUser(userDto1);
        UserDto newUserDto = userService.addUser(userDto2);
        ItemDto newItemDto = itemService.addItem(itemDto, ownerDto.getId());

        BookingInputDto bookingInputDto = BookingInputDto
                .builder()
                .itemId(newItemDto.getId())
                .start(LocalDateTime.now().plusSeconds(1))
                .end(LocalDateTime.now().plusSeconds(3))
                .build();

        BookingDto bookingDto = bookingService.addBooking(bookingInputDto, newUserDto.getId());
        bookingService.updateBooking(bookingDto.getId(), ownerDto.getId(), true);
        try {
            sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        CommentDto commentDto = CommentDto
                .builder()
                .id(1L)
                .text("Comment1")
                .itemId(newItemDto.getId())
                .authorName(newUserDto.getName())
                .created(LocalDateTime.now())
                .build();

        itemService.addComment(commentDto, newItemDto.getId(), newUserDto.getId());
        Assertions.assertEquals(1, itemService.getCommentsByItemId(newItemDto.getId()).size());
    }
}