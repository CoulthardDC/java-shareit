package ru.practicum.shareit.item;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

@JsonTest
public class ItemDtoTest {

    @Autowired
    private JacksonTester<ItemDto> json;

    @Test
    public void testItemDto() throws Exception {
        UserDto userDto = UserDto
                .builder()
                .id(1L)
                .email("example@example.ru")
                .name("name")
                .build();

        ItemDto itemDto = ItemDto
                .builder()
                .id(1L)
                .name("name")
                .description("description")
                .owner(userDto)
                .available(true)
                .build();

        JsonContent<ItemDto> result = json.write(itemDto);
        Assertions.assertThat(result)
                .extractingJsonPathNumberValue("$.id").isEqualTo(1);
        Assertions.assertThat(result)
                .extractingJsonPathStringValue("$.name").isEqualTo("name");
        Assertions.assertThat(result)
                .extractingJsonPathStringValue("$.description").isEqualTo("description");
        Assertions.assertThat(result)
                .extractingJsonPathBooleanValue("$.available").isEqualTo(true);
    }
}
