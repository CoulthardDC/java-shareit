package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;

import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.CreateException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository mockUserRepository;
    private UserService userService;

    @Mock
    private UserMapper mockMapper;

    private final UserDto userDto = UserDto
            .builder()
            .id(1L)
            .name("name")
            .email("email@example.com")
            .build();

    private final User user = User
            .builder()
            .id(1L)
            .name("name")
            .email("email@example.com")
            .build();


    @BeforeEach
    void beforeEach() {
        userService = new UserServiceImpl(mockUserRepository, mockMapper);
    }

    @Test
    void shouldExceptionWhenGetUserWithWrongId() {
        when(mockUserRepository.findById(any(Long.class)))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(
                NotFoundException.class,
                () -> userService.getUserById(-1L));
    }

    @Test
    void shouldExceptionWhenCreateUserWithExistEmail() {
        when(mockUserRepository.save(any()))
                .thenThrow(new CreateException("Ошибка при создании пользователя"));
        Assertions.assertThrows(
                CreateException.class,
                () -> userService.addUser(userDto));
    }

    @Test
    void shouldReturnUserWhenFindUserById() {
        when(mockUserRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(user));
        when(mockMapper.toUserDto(any()))
                .thenReturn(userDto);
        UserDto userDto1 = userService.getUserById(1L);
        verify(mockUserRepository, Mockito.times(1))
                .findById(1L);
        assertThat(userDto1.getName(), equalTo(userDto.getName()));
        assertThat(userDto1.getEmail(), equalTo(userDto.getEmail()));
    }

}