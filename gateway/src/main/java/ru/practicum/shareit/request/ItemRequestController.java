package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;


@RestController
@RequestMapping(path = "/requests")
@Slf4j
@Validated
public class ItemRequestController {
    private static final String X_HEADER = "X-Sharer-User-Id";
    private final RequestClient requestClient;

    @Autowired
    public ItemRequestController(RequestClient requestClient) {
        this.requestClient = requestClient;
    }

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody ItemRequestDto requestDto,
                                    @RequestHeader(X_HEADER) long userId) {
        return requestClient.createRequest(userId, requestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsersRequests(@RequestHeader(X_HEADER) long userId) {
        return requestClient.getAllUsersRequest(userId);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<Object> getAllRequests(@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                            @Positive @RequestParam(name = "size",
                                                    required = false,
                                                    defaultValue = "10") int size,
                                            @RequestHeader(X_HEADER) long userId) {
        return requestClient.getAllRequests(userId, from, size);
    }

    @GetMapping(value = "/{requestId}")
    public ResponseEntity<Object> getRequestById(@PathVariable(value = "requestId") long requestId,
                                            @RequestHeader(X_HEADER) long userId) {
        return requestClient.getRequestById(userId, requestId);
    }
}
