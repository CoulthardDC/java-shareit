package ru.practicum.shareit.user.dao.impl;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserDaoInMemoryImpl implements UserDao {
    Map<Long, User> users = new HashMap<>();
    Long id = 0L;

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User findUserById(Long userId) {
        return users.get(id);
    }

    @Override
    public User save(User user) {
        user.setId(++id);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        return users.replace(user.getId(), user);
    }

    @Override
    public void removeById(Long userId) {
        users.remove(userId);
    }
}
