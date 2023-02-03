package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    public List<User> getAllUsers();

    public User getUserById(Long userId);

    public User addUser(User user);

    public User updateUser(User user);

    public void removeUser(Long userId);
}
