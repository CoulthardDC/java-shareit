package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestServiceTest {
    private final ItemRequestService itemRequestService;

    private final UserService userService;
    private final UserDto userDto1 = UserDto
            .builder()
            .id(101L)
            .email("example1@example.com")
            .name("name1")
            .build();
    private final UserDto userDto2 = UserDto
            .builder()
            .id(102L)
            .email("example2@example.com")
            .name("name2")
            .build();

    private final ItemRequestDto itemRequestDto = ItemRequestDto
            .builder()
            .id(100L)
            .description("ItemRequest description")
            .requestor(userDto1)
            .created(LocalDateTime.of(2023, 1, 2, 3, 4, 5))
            .build();

    @Test
    void shouldCreateItemRequest() {
        UserDto newUserDto = userService.addUser(userDto1);
        ItemRequestDto returnRequestDto = itemRequestService.addRequest(itemRequestDto, newUserDto.getId(),
                LocalDateTime.of(2022, 1, 2, 3, 4, 5));
        assertThat(returnRequestDto.getDescription(), equalTo(itemRequestDto.getDescription()));
    }

    @Test
    void shouldExceptionWhenCreateItemRequestWithWrongUserId() {
       assertThrows(NotFoundException.class,
                () -> itemRequestService.addRequest(itemRequestDto, -2L,
                        LocalDateTime.of(2022, 1, 2, 3, 4, 5)));
    }

    @Test
    void shouldExceptionWhenGetItemRequestWithWrongId() {
        UserDto firstUserDto = userService.addUser(userDto1);
        assertThrows(NotFoundException.class,
                () -> itemRequestService.getRequestById(-2L, firstUserDto.getId()));
    }

    @Test
    void shouldReturnAllItemRequestsWhenSizeNotNullAndNull() {
        UserDto firstUserDto = userService.addUser(userDto1);
        UserDto newUserDto = userService.addUser(userDto2);
        itemRequestService.addRequest(itemRequestDto, newUserDto.getId(),
                LocalDateTime.of(2023, 1, 2, 3, 4, 5));
        itemRequestService.addRequest(itemRequestDto, newUserDto.getId(),
                LocalDateTime.of(2024, 1, 2, 3, 4, 5));
        List<ItemRequestDto> listItemRequest = itemRequestService.getAllRequests(firstUserDto.getId(),
                0, 10);
        assertThat(listItemRequest.size(), equalTo(2));
    }

    @Test
    void shouldReturnOwnItemRequests() {
        userService.addUser(userDto1);
        UserDto newUserDto = userService.addUser(userDto2);
        itemRequestService.addRequest(itemRequestDto, newUserDto.getId(),
                LocalDateTime.of(2023, 1, 2, 3, 4, 5));
        itemRequestService.addRequest(itemRequestDto, newUserDto.getId(),
                LocalDateTime.of(2024, 1, 2, 3, 4, 5));
        List<ItemRequestDto> listItemRequest = itemRequestService.getAllRequestsByUser(newUserDto.getId());
        System.out.println(listItemRequest.toString());
        assertThat(listItemRequest.size(), equalTo(2));
    }

    @Test
    void shouldReturnItemRequestById() {
        UserDto firstUserDto = userService.addUser(userDto1);
        ItemRequestDto newItemRequestDto = itemRequestService.addRequest(itemRequestDto, firstUserDto.getId(),
                LocalDateTime.of(2023, 1, 2, 3, 4, 5));
        ItemRequestDto returnItemRequestDto = itemRequestService.getRequestById(newItemRequestDto.getId(),
                firstUserDto.getId());
        assertThat(returnItemRequestDto.getDescription(), equalTo(itemRequestDto.getDescription()));
    }
}