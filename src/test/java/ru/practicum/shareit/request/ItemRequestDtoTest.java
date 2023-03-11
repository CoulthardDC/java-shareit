package ru.practicum.shareit.request;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@JsonTest
public class ItemRequestDtoTest {

    @Autowired
    private JacksonTester<ItemRequestDto> json;

    @Test
    public void testItemRequestDto() throws Exception {
        UserDto userDto = UserDto
                .builder()
                .id(1L)
                .email("example@example.com")
                .name("name")
                .build();

        ItemRequestDto itemRequestDto = ItemRequestDto
                .builder()
                .id(1L)
                .description("description")
                .requestor(userDto)
                .created(LocalDateTime.of(2023, 3, 11, 12, 0))
                .build();

        JsonContent<ItemRequestDto> result = json.write(itemRequestDto);
        Assertions.assertThat(result)
                .extractingJsonPathNumberValue("$.id").isEqualTo(1);
        Assertions.assertThat(result)
                .extractingJsonPathStringValue("$.description").isEqualTo("description");
        Assertions.assertThat(result)
                .extractingJsonPathStringValue("$.created").isEqualTo("2023-03-11T12:00:00");
    }
}
