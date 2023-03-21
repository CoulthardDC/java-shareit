package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {

    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getUsers() {
        log.info("Получен запрос к эндпоинту: {} {}", "GET", "/users");
        return userClient.getUsers();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findUserById(@PathVariable(value = "id") long userId) {
        log.info("Получен запрос к эндпоинту: {} /users/{}", "GET",userId);
        return userClient.getUserById(userId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@Valid @RequestBody UserDto userDto) {
        log.info("Получен запрос к эндпоинту: {} /users", "POST");
        return userClient.createUser(userDto);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<?> update(@PathVariable(value = "id") long userId,
                                    @RequestBody
                                    UserDto userDto) {
        log.info("Получен запрос к эндпоинту: {} /users/{}", "PATCH", userId);
        return userClient.updateUser(userId, userDto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> removeUser(@PathVariable(value = "id") long userId) {
        log.info("Получен запрос к эндпоинту: {} /users/{}", "DELETE", userId);
        return userClient.deleteUser(userId);
    }
}
