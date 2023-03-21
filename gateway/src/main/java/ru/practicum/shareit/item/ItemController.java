package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/items")
@Slf4j
@Validated
public class ItemController {
    private static final String X_HEADER = "X-Sharer-User-Id";
    private final ItemClient itemClient;

    @Autowired
    public ItemController(ItemClient itemClient) {
        this.itemClient = itemClient;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody ItemDto itemDto,
                                    @RequestHeader(X_HEADER) long ownerId) {
        log.info("Получен запрос к эндпоинту: {} /items", "POST");
        return itemClient.createItem(ownerId, itemDto);
    }

    @PatchMapping(value = "/{itemId}")
    public ResponseEntity<?> update(@RequestBody ItemDto itemDto,
                                    @RequestHeader(X_HEADER) long ownerId,
                                    @PathVariable(value = "itemId") long itemId) {
        log.info("Получен запрос к эндпоинту: {} /items/{}", "PATCH", itemId);
        return itemClient.updateItem(ownerId, itemId, itemDto);
    }

    @GetMapping(value = "/{itemId}")
    public ResponseEntity<?> findItemById(@PathVariable(value = "itemId") long itemId,
                                          @RequestHeader(X_HEADER) Long userId) {
        log.info("Получен запрос к эндпоинту: {} /items/{}", "GET", itemId);
        return itemClient.getItemById(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<?> findItemsByOwner(@RequestHeader(X_HEADER) long ownerId,
                                              @PositiveOrZero
                                              @RequestParam (name = "from", defaultValue = "0") int from,
                                              @Positive
                                              @RequestParam(name = "size", defaultValue = "10", required = false) int size) {
        log.info("Получен запрос к эндпоинту: {} /items", "GET");
        return itemClient.getItemsByOwner(ownerId, from, size);
    }

    @GetMapping(value = "/search")
    public ResponseEntity<?> searchItems(
            @RequestParam(value = "text", defaultValue = "") String text,
            @PositiveOrZero
            @RequestParam(name = "from", defaultValue = "0") int from,
            @Positive
            @RequestParam(name = "size", defaultValue = "10", required = false) int size) {
        log.info("Получен запрос к эндпоинту: {} /items/search", "GET");
        return itemClient.getItemsBySearch(text, from, size);
    }

    @PostMapping(value = "/{itemId}/comment")
    public ResponseEntity<?> addComment(@PathVariable(value = "itemId") long itemId,
                                        @RequestHeader(X_HEADER) Long authorId,
                                        @Valid @RequestBody CommentDto commentDto) {
        log.info("Получен запрос к эндпоинту: {} {}", "POST",
                String.format("%d/comment", itemId));
        return itemClient.addComment(authorId, itemId, commentDto);
    }
}