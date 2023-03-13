package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Objects;

public class BookingTest {
    User owner = User
            .builder()
            .id(1L)
            .name("ownerName")
            .email("owner@email.com")
            .build();

    User booker = User
            .builder()
            .id(2L)
            .name("bookerName")
            .email("booker@email.com")
            .build();

    Item item = Item
            .builder()
            .id(1L)
            .name("itemName")
            .description("itemDescription")
            .owner(owner)
            .available(true)
            .build();

    Booking booking = Booking
            .builder()
            .id(1L)
            .start(LocalDateTime.now())
            .end(LocalDateTime.now())
            .item(item)
            .booker(booker)
            .build();

    Booking booking2 = Booking
            .builder()
            .id(1L)
            .start(LocalDateTime.now())
            .end(LocalDateTime.now())
            .item(item)
            .booker(booker)
            .build();

    @Test
    public void bookingEqualsTest() {
        Assertions.assertTrue(booking.equals(booking));
        Assertions.assertFalse(booking.equals(null));
        Assertions.assertTrue(booking.equals(booking2));
    }

    @Test
    public void bookingToStringTest() {
        Assertions.assertEquals("Booking{id=1}",booking.toString());
    }

    @Test
    public void bookingSetStartAndSetEndTest() {
        LocalDateTime localDateTime = LocalDateTime.now();
        booking.setStart(localDateTime);
        booking.setEnd(localDateTime);

        Assertions.assertEquals(localDateTime, booking.getStart());
        Assertions.assertEquals(localDateTime, booking.getEnd());
    }

    @Test
    public void bookingHashCodeTest() {
        Assertions.assertEquals(Objects.hash(booking.getId()), booking.hashCode());
    }
}
