package ru.practicum.shareit.request;

import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;


@RestController
@RequestMapping(path = "/requests")
@Slf4j
@Validated
@Tag(name = "ItemRequest-контроллен",
        description = "Взаимодействие с запросами на вещь")
public class ItemRequestController {
    private static final String X_HEADER = "X-Sharer-User-Id";
    private final ItemRequestService requestService;

    @Autowired
    public ItemRequestController(ItemRequestService requestService) {
        this.requestService = requestService;
    }

    @Operation(
            summary = "Создание запроса",
            description = "Создание запроса на вешь"
    )
    @ApiResponse(
        responseCode = "200",
        content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ItemRequestDto.class))
    )
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody ItemRequestDto requestDto,
                                    @RequestHeader(X_HEADER) long userId) {
        return new ResponseEntity<>(
                requestService.addRequest(requestDto, userId, LocalDateTime.now()),
                HttpStatus.OK);
    }

    @Operation(
            summary = "Полчуние всех запросов юзера",
            description = "Полчучение всех запросов на вещь по user-id"
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema( schema = @Schema(implementation = ItemRequestDto.class)))
    )
    @GetMapping
    public ResponseEntity<?> getAllUsersRequests(@RequestHeader(X_HEADER) long userId) {
        return new ResponseEntity<>(
                requestService.getAllRequestsByUser(userId),
                HttpStatus.OK);
    }

    @Operation(
            summary = "Получение всех запросов",
            description = "Получение всех запросов на вещи"
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = ItemRequestDto.class)))
    )
    @GetMapping(value = "/all")
    public ResponseEntity<?> getAllRequests(@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                            @Positive @RequestParam(name = "size",
                                                    required = false,
                                                    defaultValue = "10") int size,
                                            @RequestHeader(X_HEADER) long userId) {
        return new ResponseEntity<>(
                requestService.getAllRequests(userId, from, size),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Получение запроса по id",
            description = "Получение запроса на вещь по id"
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ItemRequestDto.class))
    )
    @GetMapping(value = "/{requestId}")
    public ResponseEntity<?> getRequestById(@PathVariable(value = "requestId") long requestId,
                                            @RequestHeader(X_HEADER) long userId) {
        return new ResponseEntity<>(
                requestService.getRequestById(requestId, userId),
                HttpStatus.OK);
    }
}
