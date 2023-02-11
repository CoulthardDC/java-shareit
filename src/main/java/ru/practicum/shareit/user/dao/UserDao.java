package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    List<User> findAll();

    Optional<User> findUserById(Long userId);

    Optional<User> save(User user);

    Optional<User> update(User user);

    Optional<User> removeById(Long userId);
}
