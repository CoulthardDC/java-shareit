package ru.practicum.shareit.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/users")
@Slf4j
@Tag(name = "User-контроллер",
        description = "Взаимодействие с пользователями")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Получение всех пользователей",
            description = "Позволяет получить список всех пользователей"
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema =
                    @Schema(implementation = UserDto.class))))
    @GetMapping
    public ResponseEntity<?> findAll() {
        log.info("Получен запрос к эндпоинту: {} {}", "GET", "/users");
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @Operation(
            summary = "Получение пользователя по id",
            description = "Получение конкретного пользователя по id"
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserDto.class)))
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findUserById(@PathVariable(value = "id")
                                              @Parameter(description = "id пользователя",
                                                      required = true)
                                              long userId) {
        log.info("Получен запрос к эндпоинту: {} /users/{}", "GET",userId);
        return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
    }

    @Operation(
            summary = "Создание пользователя",
            description = "Создание пользователя с уникальным id"
    )
    @ApiResponse(
            responseCode = "201",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserDto.class)))
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@Valid @RequestBody UserDto userDto) {
        log.info("Получен запрос к эндпоинту: {} /users", "POST");
        return new ResponseEntity<>(userService.addUser(userDto), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Обновление пользователя",
            description = "Обновление пользователя по id"
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserDto.class)))
    @PatchMapping(value = "/{id}")
    public ResponseEntity<?> update(@PathVariable(value = "id")
                                        @Parameter(description = "id пользователя",
                                                required = true)
                                        long userId,
                                    @RequestBody
                                    UserDto userDto) {
        log.info("Получен запрос к эндпоинту: {} /users/{}", "PATCH", userId);
        return new ResponseEntity<>(userService.updateUser(userDto, userId), HttpStatus.OK);
    }

    @Operation(
            summary = "Удаление пользователя",
            description = "Удаление пользователя по id"
    )
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> removeUser(@PathVariable(value = "id")
                                            @Parameter (description = "id пользователя")
                                            long userId) {
        userService.removeUser(userId);
        log.info("Получен запрос к эндпоинту: {} /users/{}", "DELETE", userId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
