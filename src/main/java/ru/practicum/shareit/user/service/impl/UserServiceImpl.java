package ru.practicum.shareit.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserCreateException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.exception.UserRemoveException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userDao.findAll()
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long userId) {
        User user = getUserOrElseThrow(userDao.findUserById(userId), userId);
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto addUser (User user) {
        User addedUser = createOrElseThrow(userDao.save(user));
        return UserMapper.toUserDto(addedUser);
    }

    @Override
    public UserDto updateUser(User user, Long userId) {
        user.setId(userId);
        User updatedUser = createOrElseThrow(userDao.update(user));
        return UserMapper.toUserDto(updatedUser);
    }

    @Override
    public void removeUser(Long userId) {
        if (userDao.removeById(userId).isEmpty()) {
            throw new UserRemoveException();
        }
    }

    private User getUserOrElseThrow(Optional<User> optionalUser, Long userId) {
        return optionalUser.orElseThrow(
                () -> new UserNotFoundException(userId)
        );
    }

    private User createOrElseThrow(Optional<User> optionalUser) {
        return optionalUser.orElseThrow(
                UserCreateException::new
        );
    }
}
