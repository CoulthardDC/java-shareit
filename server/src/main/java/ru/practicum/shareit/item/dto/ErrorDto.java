package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ErrorDto {
    String error;
    String description;

    public ErrorDto(String error, String description) {
        this.error = error;
        this.description = description;
    }

    public String getError() {
        return error;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorDto errorDto = (ErrorDto) o;
        return Objects.equals(error, errorDto.error) && Objects.equals(description, errorDto.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(error, description);
    }

    @Override
    public String toString() {
        return "ErrorDto{" +
                "error='" + error + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}