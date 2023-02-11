package ru.practicum.shareit.user.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorDto {
    String error;
    String description;

    public ErrorDto(String error, String description) {
        this.error = error;
        this.description = description;
    }
}
