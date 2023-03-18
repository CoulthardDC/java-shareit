package ru.practicum.shareit.item;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Objects;

@JsonTest
public class ItemDtoTest {

    @Autowired
    private JacksonTester<ItemDto> json;

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

    ItemDto itemDto2 = ItemDto
            .builder()
            .id(1L)
            .name("name")
            .description("description")
            .owner(userDto)
            .available(true)
            .build();

    @Test
    public void testItemDto() throws Exception {
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

    @Test
    public void itemDtoSetTest() {
        itemDto.setRequestId(3L);
        itemDto.setId(1L);
        org.junit.jupiter.api.Assertions.assertEquals(3L, itemDto.getRequestId());
        org.junit.jupiter.api.Assertions.assertEquals(1L, itemDto.getId());
    }

    @Test
    public void itemDtoEqualsTest() {
        org.junit.jupiter.api.Assertions.assertTrue(itemDto.equals(itemDto));
        org.junit.jupiter.api.Assertions.assertFalse(itemDto.equals(null));
        org.junit.jupiter.api.Assertions.assertTrue(itemDto.equals(itemDto2));
    }

    @Test
    public void itemDtoHashCodeTest() {
        org.junit.jupiter.api.Assertions.assertEquals(Objects.hash(itemDto.getId()), itemDto.hashCode());
    }
}
