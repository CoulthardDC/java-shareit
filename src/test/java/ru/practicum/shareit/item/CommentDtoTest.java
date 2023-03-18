package ru.practicum.shareit.item;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.CommentDto;

import java.time.LocalDateTime;

@JsonTest
public class CommentDtoTest {

    @Autowired
    private JacksonTester<CommentDto> json;

    @Test
    public void testCommentDto() throws Exception {
        CommentDto commentDto = CommentDto
                .builder()
                .id(1L)
                .text("text")
                .itemId(1L)
                .authorName("name")
                .created(LocalDateTime.of(2023, 3, 11, 12, 0))
                .build();

        JsonContent<CommentDto> result = json.write(commentDto);

        Assertions.assertThat(result)
                .extractingJsonPathNumberValue("$.id").isEqualTo(1);
        Assertions.assertThat(result)
                .extractingJsonPathStringValue("$.text").isEqualTo("text");
        Assertions.assertThat(result)
                .extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        Assertions.assertThat(result)
                .extractingJsonPathStringValue("$.authorName").isEqualTo("name");
        Assertions.assertThat(result)
                .extractingJsonPathStringValue("$.created").isEqualTo("2023-03-11T12:00:00");
    }

}
