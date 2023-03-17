package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTest {
    @Mock
    BookingRepository mockBookingRepository;

    @Mock
    UserRepository mockUserRepository;


    @Test
    void shouldExceptionWhenGetBookingWithWrongId() {
        User user = User
                .builder()
                .id(1L)
                .name("name")
                .email("email@example.com")
                .build();

        BookingService bookingService = new BookingServiceImpl(mockBookingRepository,
                mockUserRepository,
                null,
                null);
        when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(mockBookingRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(
                NotFoundException.class,
                () -> bookingService.getBookingById(-1L, 1L));
    }
}