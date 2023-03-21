package ru.practicum.shareit.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(Long id) {
        super(String.format("Объект с id = %d не найден", id));
    }
}
