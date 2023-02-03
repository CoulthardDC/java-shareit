package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserDao {
    List<User> findAll();

    User findUserById(Long userId);

    User save(User user);

    User update(User user);

    void removeById(Long userId);
}
