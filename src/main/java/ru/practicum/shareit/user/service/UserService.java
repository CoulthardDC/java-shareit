package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    public List<UserDto> getAllUsers();

    public UserDto getUserById(Long userId);

    public UserDto addUser(User user);

    public UserDto updateUser(User user, Long userId);

    public void removeUser(Long userId);
}
