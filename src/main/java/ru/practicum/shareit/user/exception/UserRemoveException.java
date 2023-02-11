package ru.practicum.shareit.user.exception;

public class UserRemoveException extends RuntimeException {
    public UserRemoveException() {
        super("Ошибка при удалении пользователя");
    }
}
