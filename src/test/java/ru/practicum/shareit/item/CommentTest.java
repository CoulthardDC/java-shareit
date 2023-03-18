package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Objects;

public class CommentTest {
    User author = User
            .builder()
            .id(2L)
            .name("bookerName")
            .email("booker@email.com")
            .build();

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

    Comment comment = Comment
            .builder()
            .id(1L)
            .text("commentText")
            .item(item)
            .author(author)
            .created(LocalDateTime.now())
            .build();

    Comment comment2 = Comment
            .builder()
            .id(1L)
            .text("commentText")
            .item(item)
            .author(author)
            .created(LocalDateTime.now())
            .build();

    @Test
    public void commentEqualsTest() {
        Assertions.assertTrue(comment.equals(comment));
        Assertions.assertFalse(comment.equals(null));
        Assertions.assertTrue(comment.equals(comment2));
    }

    @Test
    public void commentToStringTest() {
        Assertions.assertEquals("Comment{" +
                "id=" + comment.getId() +
                ", text='" + comment.getText() + '\'' +
                '}', comment.toString());
    }

    @Test
    public void commentSetTest() {
        comment.setId(1L);
        comment.setText("commentText");
        comment.setItem(item);
        comment.setAuthor(author);
        LocalDateTime localDateTime = comment.getCreated();
        comment.setCreated(localDateTime);
        Assertions.assertEquals(1L, comment.getId());
        Assertions.assertEquals("commentText", comment.getText());
        Assertions.assertEquals(item, comment.getItem());
        Assertions.assertEquals(author, comment.getAuthor());
    }

    @Test
    public void bookingHashCodeTest() {
        Assertions.assertEquals(Objects.hash(comment.getId()), comment.hashCode());
    }
}
