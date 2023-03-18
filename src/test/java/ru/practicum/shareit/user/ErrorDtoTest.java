package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.ErrorDto;

import java.util.Objects;

public class ErrorDtoTest {
    ErrorDto errorDto = new ErrorDto("error", "message");
    ErrorDto errorDto2 = new ErrorDto("error", "message");

    @Test
    public void errorEqualsTest() {
        Assertions.assertTrue(errorDto.equals(errorDto));
        Assertions.assertFalse(errorDto.equals(null));
        Assertions.assertTrue(errorDto.equals(errorDto2));
    }

    @Test
    public void errorGetDescriptionAndErrorTest() {
        Assertions.assertEquals("message", errorDto.getDescription());
        Assertions.assertEquals("error", errorDto.getError());
    }

    @Test
    public void errorToStringTest() {
        Assertions.assertEquals(
                "ErrorDto{" +
                        "error='" + errorDto.getError() + '\'' +
                        ", description='" + errorDto.getDescription() + '\'' +
                        '}', errorDto.toString()
        );
    }

    @Test
    public void errorHashCodeTest() {
        Assertions.assertEquals(Objects.hash(errorDto.getError(), errorDto.getDescription()), errorDto.hashCode());
    }
}
