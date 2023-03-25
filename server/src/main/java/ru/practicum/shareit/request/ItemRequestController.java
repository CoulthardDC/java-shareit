package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.time.LocalDateTime;


@RestController
@RequestMapping(path = "/requests")
@Slf4j
public class ItemRequestController {
    private static final String X_HEADER = "X-Sharer-User-Id";
    private final ItemRequestService requestService;

    @Autowired
    public ItemRequestController(ItemRequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ItemRequestDto requestDto,
                                    @RequestHeader(X_HEADER) long userId) {
        return new ResponseEntity<>(
                requestService.addRequest(requestDto, userId, LocalDateTime.now()),
                HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllUsersRequests(@RequestHeader(X_HEADER) long userId) {
        return new ResponseEntity<>(
                requestService.getAllRequestsByUser(userId),
                HttpStatus.OK);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<?> getAllRequests(@RequestParam(name = "from", defaultValue = "0") int from,
                                            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
                                            @RequestHeader(X_HEADER) long userId) {
        return new ResponseEntity<>(
                requestService.getAllRequests(userId, from, size),
                HttpStatus.OK
        );
    }

    @GetMapping(value = "/{requestId}")
    public ResponseEntity<?> getRequestById(@PathVariable(value = "requestId") long requestId,
                                            @RequestHeader(X_HEADER) long userId) {
        return new ResponseEntity<>(
                requestService.getRequestById(requestId, userId),
                HttpStatus.OK);
    }
}
