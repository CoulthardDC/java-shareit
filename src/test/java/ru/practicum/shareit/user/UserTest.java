package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;

public class UserTest {
    User user = User
            .builder()
            .id(1L)
            .email("example@example.com")
            .name("name")
            .build();

    User user2 = User
            .builder()
            .id(1L)
            .email("example@example.com")
            .name("name")
            .build();

    @Test
    public void userEqualsTest() {
        Assertions.assertTrue(user.equals(user));
        Assertions.assertFalse(user.equals(null));
        Assertions.assertTrue(user.equals(user2));
    }

    @Test
    public void userHashCodeTest() {
        Assertions.assertEquals(Objects.hash(user.getId()), user.hashCode());
    }
}
