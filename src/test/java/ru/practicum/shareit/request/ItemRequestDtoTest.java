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
import java.util.Objects;

@JsonTest
public class ItemRequestDtoTest {
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

    ItemRequestDto itemRequestDto2 = ItemRequestDto
            .builder()
            .id(1L)
            .description("description")
            .requestor(userDto)
            .created(LocalDateTime.of(2023, 3, 11, 12, 0))
            .build();

    @Autowired
    private JacksonTester<ItemRequestDto> json;

    @Test
    public void testItemRequestDto() throws Exception {
        JsonContent<ItemRequestDto> result = json.write(itemRequestDto);
        Assertions.assertThat(result)
                .extractingJsonPathNumberValue("$.id").isEqualTo(1);
        Assertions.assertThat(result)
                .extractingJsonPathStringValue("$.description").isEqualTo("description");
        Assertions.assertThat(result)
                .extractingJsonPathStringValue("$.created").isEqualTo("2023-03-11T12:00:00");
    }

    @Test
    public void itemRequestDtoEqualsTest() {
        org.junit.jupiter.api.Assertions.assertTrue(itemRequestDto.equals(itemRequestDto));
        org.junit.jupiter.api.Assertions.assertFalse(itemRequestDto.equals(null));
        org.junit.jupiter.api.Assertions.assertTrue(itemRequestDto.equals(itemRequestDto2));
    }

    @Test
    public void itemRequestDtoHashCodeTest() {
        org.junit.jupiter.api.Assertions.assertEquals(Objects.hash(itemRequestDto.getId()), itemRequestDto.hashCode());
    }

    @Test
    public void itemRequestSetTest() {
        LocalDateTime created = itemRequestDto.getCreated();

        itemRequestDto.setDescription("description");
        itemRequestDto.setRequestor(userDto);
        itemRequestDto.setId(1L);
        itemRequestDto.setCreated(created);

        org.junit.jupiter.api.Assertions.assertEquals(1L, itemRequestDto.getId());
        org.junit.jupiter.api.Assertions.assertEquals("description", itemRequestDto.getDescription());
        org.junit.jupiter.api.Assertions.assertEquals(userDto, itemRequestDto.getRequestor());
        org.junit.jupiter.api.Assertions.assertEquals(created, itemRequestDto.getCreated());
    }
}
