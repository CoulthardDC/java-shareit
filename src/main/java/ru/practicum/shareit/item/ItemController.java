package ru.practicum.shareit.item;

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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/items")
@Slf4j
@Tag(name = "Item-контроллер",
        description = "Взаимодействие с вещами")
public class ItemController {
    private final ItemService itemService;
    private static final String X_HEADER = "X-Sharer-User-Id";

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @Operation(
            summary = "Добавление вещи",
            description = "Позволяет добавить вещь"
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ItemDto.class)))
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody ItemDto itemDto,
                                    @RequestHeader(X_HEADER) long ownerId,
                                    HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: {} {}", request.getMethod(),
                request.getRequestURL());
        return new ResponseEntity<>(itemService.addItem(itemDto, ownerId), HttpStatus.OK);
    }

    @Operation(
            summary = "Обновление вещи",
            description = "Позволяет обновить вещь по id"
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ItemDto.class)))
    @PatchMapping(value = "/{itemId}")
    public ResponseEntity<?> update(@RequestBody ItemDto itemDto,
                                    @RequestHeader(X_HEADER) long ownerId,
                                    @PathVariable(value = "itemId")
                                    @Parameter(description = "id вещи",
                                            required = true)
                                    long itemId,
                                    HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: {} {}", request.getMethod(),
                request.getRequestURL());
        return new ResponseEntity<>(itemService.updateItem(itemDto, itemId, ownerId),
                HttpStatus.OK);
    }

    @Operation(
            summary = "Получение вещи по id",
            description = "Получение конкретной вещи по id"
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ItemDto.class)))
    @GetMapping(value = "/{itemId}")
    public ResponseEntity<?> findItemById(@PathVariable(value = "itemId")
                                          @Parameter(description = "id вещи",
                                                  required = true)
                                          long itemId,
                                          HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: {} {}", request.getMethod(),
                request.getRequestURL());
        return new ResponseEntity<>(itemService.getItemById(itemId), HttpStatus.OK);
    }

    @Operation(
            summary = "Получение списка вещей владельца",
            description = "Получение списка вещей по id владельца"
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema =
                    @Schema(implementation = ItemDto.class))))
    @GetMapping
    public ResponseEntity<?> findItemsByOwner(@RequestHeader(X_HEADER) long ownerId,
                                              HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: {} {}", request.getMethod(),
                request.getRequestURL());
        return new ResponseEntity<>(itemService.getItemsByOwner(ownerId), HttpStatus.OK);
    }

    @Operation(
            summary = "Поиск вещей",
            description = "Поиск вещей по названию и описинию"
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema =
                    @Schema(implementation = ItemDto.class))))
    @GetMapping(value = "/search")
    public ResponseEntity<?> searchItems(
            @RequestParam(value = "text", defaultValue = "") String text,
            HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: {} {}", request.getMethod(),
                request.getRequestURL());
        return new ResponseEntity<>(itemService.getItemsBySearch(text), HttpStatus.OK);
    }
}