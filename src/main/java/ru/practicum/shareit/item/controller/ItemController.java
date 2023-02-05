package ru.practicum.shareit.item.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;

@RestController
@RequestMapping("/items")
@Slf4j
public class ItemController {
    private final ItemService itemService;
    private static final String X_HEADER = "X-Sharer-User-Id";

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody ItemDto itemDto,
                                    @RequestHeader(X_HEADER) long ownerId) {
        log.info("Получен запрос к эндпоинту: {} /items", "POST");
        return new ResponseEntity<>(itemService.addItem(itemDto, ownerId), HttpStatus.OK);
    }

    @PatchMapping(value = "/{itemId}")
    public ResponseEntity<?> update(@RequestBody ItemDto itemDto,
                                    @RequestHeader(X_HEADER) long ownerId,
                                    @PathVariable(value = "itemId") long itemId) {
        log.info("Получен запрос к эндпоинту: {} /items/{}", "PATCH", itemId);
        return new ResponseEntity<>(itemService.updateItem(itemDto, itemId, ownerId),
                HttpStatus.OK);
    }

    @GetMapping(value = "/{itemId}")
    public ResponseEntity<?> findItemById(@PathVariable(value = "itemId") long itemId) {
        log.info("Получен запрос к эндпоинту: {} /items/{}", "GET", itemId);
        return new ResponseEntity<>(itemService.getItemById(itemId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> findItemsByOwner(@RequestHeader(X_HEADER) long ownerId) {
        log.info("Получен запрос к эндпоинту: {} /items", "GET");
        return new ResponseEntity<>(itemService.getItemsByOwner(ownerId), HttpStatus.OK);
    }

    @GetMapping(value = "/search")
    public ResponseEntity<?> searchItems(
            @RequestParam(value = "text", defaultValue = "") String text) {
        log.info("Получен запрос к эндпоинту: {} /items/search?text={}", "GET", text);
        return new ResponseEntity<>(itemService.getItemsBySearch(text), HttpStatus.OK);
    }
}
