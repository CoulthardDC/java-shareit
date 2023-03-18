package ru.practicum.shareit.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Objects;

public class ItemRequestTest {
    User user = User
            .builder()
            .id(1L)
            .email("example@example.com")
            .name("name")
            .build();

    ItemRequest itemRequest = ItemRequest
            .builder()
            .id(1L)
            .description("description")
            .requestor(user)
            .created(LocalDateTime.of(2023, 3, 11, 12, 0))
            .build();

    ItemRequest itemRequest2 = ItemRequest
            .builder()
            .id(1L)
            .description("description")
            .requestor(user)
            .created(LocalDateTime.of(2023, 3, 11, 12, 0))
            .build();

    @Test
    public void testItemRequestEquals() {
        Assertions.assertTrue(itemRequest.equals(itemRequest));
        Assertions.assertFalse(itemRequest.equals(null));
        Assertions.assertTrue(itemRequest.equals(itemRequest2));
    }

    @Test
    public void itemRequestHashCodeTest() {
        Assertions.assertEquals(Objects.hash(itemRequest.getId()), itemRequest.hashCode());
    }

    @Test
    public void itemRequestToStringTest() {
        Assertions.assertEquals(
                "ItemRequest{" +
                        "id=" + itemRequest.getId() +
                        ", description='" + itemRequest.getDescription() + '\'' +
                        ", requestor=" + itemRequest.getRequestor() +
                        ", created=" + itemRequest.getCreated() +
                        '}', itemRequest.toString()
        );
    }

    @Test
    public void itemRequestSetDescriptionTest() {
        itemRequest.setDescription("description");
        Assertions.assertEquals("description", itemRequest.getDescription());
    }
}
