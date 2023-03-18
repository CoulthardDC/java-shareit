package ru.practicum.shareit.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ItemRequestServiceImplTest {
    @Mock
    private ItemRequestRepository mockItemRequestRepository;
    private ItemRequestService itemRequestService;
    private RequestMapper itemRequestMapper;

    @Mock
    private UserRepository userRepository;

    UserDto userDto = UserDto
            .builder()
            .id(1L)
            .name("name")
            .email("email@example.com")
            .build();

    User user = User
            .builder()
            .id(1L)
            .name("name")
            .email("email@example.com")
            .build();

    private ItemRequestDto itemRequestDto = ItemRequestDto
            .builder()
            .id(1L)
            .description("item description")
            .requestor(userDto)
            .created(LocalDateTime.of(2022, 1, 2, 3, 4, 5))
            .build();

    @BeforeEach
    void beforeEach() {
        itemRequestService = new ItemRequestServiceImpl(mockItemRequestRepository,
                userRepository, null, null, null);
    }

    @Test
    void shouldExceptionWhenGetItemRequestWithWrongId() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(mockItemRequestRepository.findById(any(Long.class)))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(
                NotFoundException.class,
                () -> itemRequestService.getRequestById(-1L, 1L));
    }
}