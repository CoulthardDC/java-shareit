package ru.practicum.shareit.user.dao.impl;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
public class UserDaoInMemoryImpl implements UserDao {
    private final Map<Long, User> users = new HashMap<>();
    private Long id = 0L;

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> findUserById(Long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public Optional<User> save(User user) {
        if (user.getId() != null || users.values()
                .stream()
                .anyMatch(u -> u.getEmail().equals(user.getEmail()))
        ) {
            return Optional.empty();
        } else {
            user.setId(++id);
            users.put(user.getId(), user);
            return Optional.of(user);
        }
    }

    @Override
    public Optional<User> update(User user) {
        if (users.containsKey(user.getId())) {
            User replacedUser = users.get(user.getId());
            if (users.values()
                    .stream()
                    .noneMatch(u -> u.getEmail().equals(user.getEmail()))
            ) {
                if (user.getName() != null && !Objects.equals(user.getName(), "")) {
                    replacedUser.setName(user.getName());
                }

                if (user.getEmail() != null && !user.getEmail().isBlank()) {
                    replacedUser.setEmail(user.getEmail());
                }
                return Optional.of(replacedUser);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> removeById(Long userId) {
        return Optional.ofNullable(users.remove(userId));
    }
}
