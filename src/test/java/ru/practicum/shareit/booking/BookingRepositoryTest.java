package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
scripts = {"/schema.sql"})
public class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    User user = User
            .builder()
            .name("name")
            .email("email@example.com")
            .build();

    User booker = User
            .builder()
            .name("booker")
            .email("booker@example.com")
            .build();

    Item item = Item
            .builder()
            .name("item name")
            .description("item description")
            .owner(user)
            .available(true)
            .build();

    Booking booking = Booking
            .builder()
            .item(item)
            .booker(booker)
            .start(LocalDateTime.of(2023, 4, 11, 12, 0))
            .end(LocalDateTime.of(2023, 4, 12, 12, 0))
            .status(Status.WAITING)
            .build();

    @BeforeEach
    public void beforeEach() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
        bookingRepository.deleteAll();

        user.setId(null);
        user = userRepository.save(user);

        booker.setId(null);
        booker = userRepository.save(booker);

        item.setId(null);
        item.setOwner(user);
        item = itemRepository.save(item);

        booking.setId(null);
        booking.setItem(item);

    }

    @Test
    void testSaveBooking() {
        Booking savedBooking = bookingRepository.save(booking);

        Assertions.assertEquals(1, savedBooking.getId());
        Assertions.assertEquals(item.getId(), savedBooking.getItem().getId());
        Assertions.assertEquals(booker.getId(), savedBooking.getBooker().getId());
    }

    @Test
    void testFindByBookerId() {
        bookingRepository.save(booking);

        List<Booking> bookings = bookingRepository.findByBookerId(
                booker.getId(),
                PageRequest.of(0, 10)
        );

        Assertions.assertEquals(1, bookings.size());
        Assertions.assertEquals(1, bookings.get(0).getId());
        Assertions.assertEquals(item.getId(), bookings.get(0).getItem().getId());
        Assertions.assertEquals(booker.getId(), bookings.get(0).getBooker().getId());
    }

    @Test
    public void testFindByBookerIdAndStartIsBeforeAndEndIsAfter() {
        bookingRepository.save(booking);

        List<Booking> bookings = bookingRepository.findByBookerIdAndStartIsBeforeAndEndIsAfter(
                booker.getId(),
                LocalDateTime.of(2023, 4, 12, 12, 0),
                LocalDateTime.of(2023, 4, 11, 12, 0),
                PageRequest.of(0, 10)
        );

        Assertions.assertEquals(1, bookings.size());
        Assertions.assertEquals(1, bookings.get(0).getId());
        Assertions.assertEquals(item.getId(), bookings.get(0).getItem().getId());
        Assertions.assertEquals(booker.getId(), bookings.get(0).getBooker().getId());
    }

    @Test
    public void testFindByBookerIdAndStartIsBeforeAndEndIsBefore() {
        bookingRepository.save(booking);

        List<Booking> bookings = bookingRepository.findByBookerIdAndStartIsBeforeAndEndIsBefore(
                booker.getId(),
                LocalDateTime.of(2023, 4, 12, 12, 0),
                LocalDateTime.of(2023, 4, 14, 12, 0),
                PageRequest.of(0, 10)
        );

        Assertions.assertEquals(1, bookings.size());
        Assertions.assertEquals(1, bookings.get(0).getId());
        Assertions.assertEquals(item.getId(), bookings.get(0).getItem().getId());
        Assertions.assertEquals(booker.getId(), bookings.get(0).getBooker().getId());
    }

    @Test
    public void testFindByBookerIdAndStartIsAfter() {
        bookingRepository.save(booking);

        List<Booking> bookings = bookingRepository.findByBookerIdAndStartIsAfter(
                booker.getId(),
                LocalDateTime.of(2023, 4, 10, 12, 0, 0),
                PageRequest.of(0, 10)
        );

        Assertions.assertEquals(1, bookings.size());
        Assertions.assertEquals(1, bookings.get(0).getId());
        Assertions.assertEquals(item.getId(), bookings.get(0).getItem().getId());
        Assertions.assertEquals(booker.getId(), bookings.get(0).getBooker().getId());
    }

    @Test
    public void testFindByBookerIdAndStatus() {
        bookingRepository.save(booking);

        List<Booking> bookings = bookingRepository.findByBookerIdAndStatus(
                booker.getId(),
                Status.WAITING,
                PageRequest.of(0, 10)
        );

        Assertions.assertEquals(1, bookings.size());
        Assertions.assertEquals(1, bookings.get(0).getId());
        Assertions.assertEquals(item.getId(), bookings.get(0).getItem().getId());
        Assertions.assertEquals(booker.getId(), bookings.get(0).getBooker().getId());
    }

    @Test
    public void testFindByItem_Owner_id() {
        bookingRepository.save(booking);

        List<Booking> bookings = bookingRepository.findByItem_Owner_id(
                user.getId(),
                PageRequest.of(0, 10)
        );

        Assertions.assertEquals(1, bookings.size());
        Assertions.assertEquals(1, bookings.get(0).getId());
        Assertions.assertEquals(item.getId(), bookings.get(0).getItem().getId());
        Assertions.assertEquals(booker.getId(), bookings.get(0).getBooker().getId());
    }

    @Test
    public void testFindByItem_Owner_IdAndStartIsBeforeAndEndIsAfter() {
        bookingRepository.save(booking);

        List<Booking> bookings = bookingRepository.findByItem_Owner_IdAndStartIsBeforeAndEndIsAfter(
                user.getId(),
                LocalDateTime.of(2023, 4, 12, 12, 0),
                LocalDateTime.of(2023, 4, 11, 12, 0),
                PageRequest.of(0, 10)
        );

        Assertions.assertEquals(1, bookings.size());
        Assertions.assertEquals(1, bookings.get(0).getId());
        Assertions.assertEquals(item.getId(), bookings.get(0).getItem().getId());
        Assertions.assertEquals(booker.getId(), bookings.get(0).getBooker().getId());
    }

    @Test
    public void testFindByItem_Owner_IdAndEndIsBefore() {
        bookingRepository.save(booking);

        List<Booking> bookings = bookingRepository.findByItem_Owner_IdAndEndIsBefore(
                user.getId(),
                LocalDateTime.of(2023, 4, 13, 12, 0),
                PageRequest.of(0, 10)
        );

        Assertions.assertEquals(1, bookings.size());
        Assertions.assertEquals(1, bookings.get(0).getId());
        Assertions.assertEquals(item.getId(), bookings.get(0).getItem().getId());
        Assertions.assertEquals(booker.getId(), bookings.get(0).getBooker().getId());
    }

    @Test
    public void testFindByItem_Owner_IdAndStartIsAfter() {
        bookingRepository.save(booking);

        List<Booking> bookings = bookingRepository.findByItem_Owner_IdAndStartIsAfter(
                user.getId(),
                LocalDateTime.of(2023, 4, 10, 12, 0),
                PageRequest.of(0, 10)
        );

        Assertions.assertEquals(1, bookings.size());
        Assertions.assertEquals(1, bookings.get(0).getId());
        Assertions.assertEquals(item.getId(), bookings.get(0).getItem().getId());
        Assertions.assertEquals(booker.getId(), bookings.get(0).getBooker().getId());
    }

    @Test
    public void testFindByItem_Owner_IdAndStatus() {
        bookingRepository.save(booking);

        List<Booking> bookings = bookingRepository.findByItem_Owner_IdAndStatus(
                user.getId(),
                Status.WAITING,
                PageRequest.of(0, 10)
        );

        Assertions.assertEquals(1, bookings.size());
        Assertions.assertEquals(1, bookings.get(0).getId());
        Assertions.assertEquals(item.getId(), bookings.get(0).getItem().getId());
        Assertions.assertEquals(booker.getId(), bookings.get(0).getBooker().getId());
    }

    @Test
    public void testFindFirstByItem_IdAndEndBeforeOrderByEndDesc() {
        bookingRepository.save(booking);

        Booking savedBooking = bookingRepository.findFirstByItem_IdAndEndBeforeOrderByEndDesc(
                item.getId(),
                LocalDateTime.of(2023, 4, 13, 12, 0)
        );

        Assertions.assertEquals(1, savedBooking.getId());
        Assertions.assertEquals(item.getId(), savedBooking.getItem().getId());
        Assertions.assertEquals(booker.getId(), savedBooking.getBooker().getId());
    }

    @Test
    public void testFindFirstByItem_IdAndStartAfterAndStatusNotOrderByStartAsc() {
        bookingRepository.save(booking);

        Booking savedBooking = bookingRepository.findFirstByItem_IdAndStartAfterAndStatusNotOrderByStartAsc(
                item.getId(),
                LocalDateTime.of(2023, 4, 1, 12, 0),
                Status.REJECTED
        );

        Assertions.assertEquals(1, savedBooking.getId());
        Assertions.assertEquals(item.getId(), savedBooking.getItem().getId());
        Assertions.assertEquals(booker.getId(), savedBooking.getBooker().getId());
    }

    @Test
    public void testFindFirstByItem_idAndBooker_IdAndEndBefore() {
        bookingRepository.save(booking);

        Booking savedBooking = bookingRepository.findFirstByItem_idAndBooker_IdAndEndBefore(
                item.getId(),
                booker.getId(),
                LocalDateTime.of(2023, 4, 15, 12, 0)
        );

        Assertions.assertEquals(1, savedBooking.getId());
        Assertions.assertEquals(item.getId(), savedBooking.getItem().getId());
        Assertions.assertEquals(booker.getId(), savedBooking.getBooker().getId());
    }
}
