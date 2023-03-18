package ru.practicum.shareit.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.dto.ErrorDto;

public class ErrorDtoTest {
    ErrorDto errorDto = new ErrorDto("error", "message");
    ErrorDto errorDto2 = new ErrorDto("error", "message");

    @Test
    public void errorDtoTest() {
        errorDto.setDescription("message");
        errorDto.setMessage("error");

        Assertions.assertEquals("message", errorDto.getDescription());
        Assertions.assertEquals("error", errorDto.getMessage());
    }
}
