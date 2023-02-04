package ru.practicum.shareit.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/users")
@Slf4j
public class UserController {
    UserService userService;

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
        log.info("Получен запрос к эндпоинту: {} /users/{}", "GET", userId);
        return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@Valid @RequestBody User user) {
        log.info("Получен запрос к энжпоинту: {} /users", "GET");
        return new ResponseEntity<>(userService.addUser(user), HttpStatus.CREATED);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<?> update(@PathVariable(value = "id") long userId,
                       @RequestBody User user) {
        log.info("Получен запрос к эндпоинту: {} /users/{}", "PATCH", userId);
        return new ResponseEntity<>(userService.updateUser(user, userId), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> removeUser (@PathVariable("id") long userId) {
        userService.removeUser(userId);
        log.info("Получен запрос к эндпоинту: {} /users/{}", "DELETE", userId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
