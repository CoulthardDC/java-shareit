package ru.practicum.shareit.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Objects;

@JsonTest
public class UserDtoTest {
    UserDto userDto = UserDto
            .builder()
            .id(1L)
            .email("example@example.com")
            .name("name")
            .build();

    UserDto userDto1 = UserDto
            .builder()
            .id(1L)
            .email("example@example.com")
            .name("name")
            .build();

    @Autowired
    private JacksonTester<UserDto> json;

    @Test
    public void testUserDto() throws Exception {
        JsonContent<UserDto> result = json.write(userDto);

        Assertions.assertThat(result)
                .extractingJsonPathNumberValue("$.id").isEqualTo(1);
        Assertions.assertThat(result)
                .extractingJsonPathStringValue("$.email").isEqualTo("example@example.com");
        Assertions.assertThat(result)
                .extractingJsonPathStringValue("$.name").isEqualTo("name");
    }

    public void itemDtoEqualsTest() {
        org.junit.jupiter.api.Assertions.assertTrue(userDto.equals(userDto));
        org.junit.jupiter.api.Assertions.assertFalse(userDto.equals(null));
        org.junit.jupiter.api.Assertions.assertTrue(userDto.equals(userDto1));
    }

    @Test
    public void itemDtoHashCodeTest() {
        org.junit.jupiter.api.Assertions.assertEquals(Objects.hash(userDto.getId()), userDto.hashCode());
    }
}
