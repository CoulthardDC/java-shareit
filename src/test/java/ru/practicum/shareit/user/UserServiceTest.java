package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.CreateException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceTest {
    private final UserService userService;
    private final UserMapper mapper;
    private User user = new User(1L, "first@first.ru", "User");

    @Test
    void shouldReturnUserWhenGetUserById() {
        UserDto returnUserDto = userService.addUser(mapper.toUserDto(user));
        assertThat(returnUserDto.getName(), equalTo(user.getName()));
        assertThat(returnUserDto.getEmail(), equalTo(user.getEmail()));
    }

    @Test
    void shouldExceptionWhenDeleteUserWithWrongId() {
        assertThrows(NotFoundException.class, () -> userService.removeUser(10L));
    }

    @Test
    void shouldDeleteUser() {
        User user = new User(10L, "ten@ten.ru", "Ten");
        UserDto returnUserDto = userService.addUser(mapper.toUserDto(user));
        List<UserDto> listUser = userService.getAllUsers();
        int size = listUser.size();
        userService.removeUser(returnUserDto.getId());
        listUser = userService.getAllUsers();
        assertThat(listUser.size(), equalTo(size - 1));
    }

    @Test
    void shouldUpdateUser() {
        UserDto returnUserDto = userService.addUser(mapper.toUserDto(user));
        returnUserDto.setName("NewName");
        returnUserDto.setEmail("new@email.ru");
        userService.updateUser(returnUserDto, returnUserDto.getId());
        UserDto updateUserDto = userService.getUserById(returnUserDto.getId());
        assertThat(updateUserDto.getName(), equalTo("NewName"));
        assertThat(updateUserDto.getEmail(), equalTo("new@email.ru"));
    }

    @Test
    void shouldExceptionWhenUpdateUserWithExistEmail() {
        user = new User(2L, "second@second.ru", "User2");
        userService.addUser(mapper.toUserDto(user));
        User newUser = new User(3L, "third@third.ru", "User3");
        UserDto returnUserDto = userService.addUser(mapper.toUserDto(newUser));
        Long id = returnUserDto.getId();
        returnUserDto.setId(null);
        returnUserDto.setEmail("second@second.ru");
        Assertions.assertThrows(
                CreateException.class,
                () -> userService.updateUser(returnUserDto, id));
    }
}
