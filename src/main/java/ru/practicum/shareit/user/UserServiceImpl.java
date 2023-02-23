package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserCreateException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long userId) {
        User user = getUserOrElseThrow(userRepository.findById(userId), userId);
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        try {
            return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(userDto)));
        } catch (DataIntegrityViolationException e) {
            throw new UserCreateException();
        }
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long userId) {
        if (userDto.getId() == null) {
            userDto.setId(userId);
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if ((userDto.getEmail() != null) && !(userDto.getEmail().equals(user.getEmail()))) {
            if (userRepository.findByEmail(userDto.getEmail())
                    .stream()
                    .filter(u -> u.getEmail().equals(userDto.getEmail()))
                    .allMatch(u -> u.getId().equals(userDto.getId()))) {
                user.setEmail(userDto.getEmail());
            } else {
                throw new UserCreateException();
            }

        }
        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public void removeUser(Long userId) {
        try {
            userRepository.deleteById(userId);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException(userId);
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
