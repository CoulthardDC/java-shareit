package ru.practicum.shareit.item.exception;

public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(Long itemId) {
        super(String.format("Вещь с id = %d не найдена", itemId));
    }
}