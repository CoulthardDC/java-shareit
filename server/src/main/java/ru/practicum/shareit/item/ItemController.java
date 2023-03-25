package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

@RestController
@RequestMapping("/items")
@Slf4j
@Validated
public class ItemController {
    private final ItemService itemService;
    private static final String X_HEADER = "X-Sharer-User-Id";

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ItemDto itemDto,
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
    public ResponseEntity<?> findItemById(@PathVariable(value = "itemId") long itemId,
                                          @RequestHeader(X_HEADER) Long userId) {
        log.info("Получен запрос к эндпоинту: {} /items/{}", "GET", itemId);
        return new ResponseEntity<>(
                itemService.getItemById(itemId, userId),
                HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> findItemsByOwner(@RequestHeader(X_HEADER) long ownerId,
                                              @RequestParam (name = "from", defaultValue = "0") int from,
                                              @RequestParam(name = "size", defaultValue = "10", required = false) int size) {
        log.info("Получен запрос к эндпоинту: {} /items", "GET");
        return new ResponseEntity<>(itemService.getItemsByOwner(ownerId, from, size), HttpStatus.OK);
    }

    @GetMapping(value = "/search")
    public ResponseEntity<?> searchItems(
            @RequestParam(value = "text", defaultValue = "") String text,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size) {
        log.info("Получен запрос к эндпоинту: {} /items/search", "GET");
        return new ResponseEntity<>(itemService.getItemsBySearch(text, from, size), HttpStatus.OK);
    }

    @PostMapping(value = "/{itemId}/comment")
    public ResponseEntity<?> addComment(@PathVariable(value = "itemId") long itemId,
                                        @RequestHeader(X_HEADER) Long authorId,
                                        @RequestBody CommentDto commentDto) {
        log.info("Получен запрос к эндпоинту: {} {}", "POST",
                String.format("%d/comment", itemId));
        return new ResponseEntity<>(
                itemService.addComment(commentDto, itemId, authorId),
                HttpStatus.OK
        );
    }
}