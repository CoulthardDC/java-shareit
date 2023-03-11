package ru.practicum.shareit.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserDto;

@JsonTest
public class UserDtoTest {

    @Autowired
    private JacksonTester<UserDto> json;

    @Test
    public void testUserDto() throws Exception {
        UserDto userDto = UserDto
                .builder()
                .id(1L)
                .email("example@example.com")
                .name("name")
                .build();

        JsonContent<UserDto> result = json.write(userDto);

        Assertions.assertThat(result)
                .extractingJsonPathNumberValue("$.id").isEqualTo(1);
        Assertions.assertThat(result)
                .extractingJsonPathStringValue("$.email").isEqualTo("example@example.com");
        Assertions.assertThat(result)
                .extractingJsonPathStringValue("$.name").isEqualTo("name");
    }
}
