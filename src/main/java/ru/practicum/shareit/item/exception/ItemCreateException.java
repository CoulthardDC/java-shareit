package ru.practicum.shareit.item.exception;

public class ItemCreateException extends RuntimeException {
    public ItemCreateException() {
        super("Невозмодно добавить вещь");
    }
}
