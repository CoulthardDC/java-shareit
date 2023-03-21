package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;


@RestController
@RequestMapping(path = "/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        log.info("Получен запрос к эндпоинту: {} {}", "GET", "/users");
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findUserById(@PathVariable(value = "id") long userId) {
        log.info("Получен запрос к эндпоинту: {} /users/{}", "GET",userId);
        return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@RequestBody UserDto userDto) {
        log.info("Получен запрос к эндпоинту: {} /users", "POST");
        return new ResponseEntity<>(userService.addUser(userDto), HttpStatus.CREATED);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<?> update(@PathVariable(value = "id") long userId,
                                    @RequestBody
                                    UserDto userDto) {
        log.info("Получен запрос к эндпоинту: {} /users/{}", "PATCH", userId);
        return new ResponseEntity<>(userService.updateUser(userDto, userId), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> removeUser(@PathVariable(value = "id") long userId) {
        userService.removeUser(userId);
        log.info("Получен запрос к эндпоинту: {} /users/{}", "DELETE", userId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
