package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.User;

import java.util.Objects;

public class ItemTest {
    User owner = User
            .builder()
            .id(1L)
            .name("ownerName")
            .email("owner@email.com")
            .build();

    Item item = Item
            .builder()
            .id(1L)
            .name("itemName")
            .description("itemDescription")
            .owner(owner)
            .available(true)
            .build();

    Item item2 = Item
            .builder()
            .id(1L)
            .name("itemName")
            .description("itemDescription")
            .owner(owner)
            .available(true)
            .build();

    @Test
    public void bookingEqualsTest() {
        Assertions.assertTrue(item.equals(item));
        Assertions.assertFalse(item.equals(null));
        Assertions.assertTrue(item.equals(item2));
    }

    @Test
    public void bookingToStringTest() {
        Assertions.assertEquals(
                "Item{" +
                        "id=" + item.getId() +
                        ", name='" + item.getName() + '\'' +
                        ", description='" + item.getDescription() + '\'' +
                        ", owner=" + owner +
                        ", available=" + item.getAvailable() +
                        ", request=" + item.getRequestId() +
                        '}', item.toString()
        );
    }

    @Test
    public void bookingHashCodeTest() {
        Assertions.assertEquals(Objects.hash(item.getId()), item.hashCode());
    }
}
