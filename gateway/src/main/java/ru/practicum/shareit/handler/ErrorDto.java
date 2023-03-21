package ru.practicum.shareit.handler;

public class ErrorDto {
    private final String error;

    public ErrorDto(String message) {
        this.error = message;
    }

    public String getError() {
        return error;
    }
}
