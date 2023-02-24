package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.exception.PermissionException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository,
                              UserRepository userRepository,
                              ItemRepository itemRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public BookingDto addBooking(BookingInputDto bookingDto, Long userId) {
        User user = getUserOrElseThrow(userRepository.findById(userId), userId);
        Item item = getAvailableItemOrElseThrow(
                itemRepository.findById(bookingDto.getItemId()),
                bookingDto.getItemId()
        );
        Booking booking = BookingMapper.toBooking(bookingDto, user, item);
        if (userId.equals(booking.getItem().getOwner().getId())) {
            throw new PermissionException("Вещь с ID=" + bookingDto.getItemId() +
                    " недоступна для бронирования самим владельцем!");
        }
        if (booking.getStart().isAfter(booking.getEnd())) {
            throw new ValidationException("Время начала аренды позже времени окончания");
        }
        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto updateBooking(Long bookingId, Long userId, Boolean approved) {
        userExistOrElseThrow(userRepository.findById(userId), userId);
        Booking booking = getBookingOrElseThrow(
                bookingRepository.findById(bookingId),
                bookingId
        );
        if (booking.getEnd().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Время бронирования истекло");
        }

        if (booking.getBooker().getId().equals(userId)) {
            if (!approved) {
                booking.setStatus(Status.CANCELED);
            } else {
                throw new PermissionException(
                        String.format("Пользователь с id=%d не может подтведить " +
                                "бронь с id = %d", userId, bookingId)
                );
            }
        } else if (userIsOwnerOfItem(booking.getItem(), userId) &&
                (!booking.getStatus().equals(Status.CANCELED))) {
            if (!booking.getStatus().equals(Status.WAITING)) {
                throw new ValidationException("Решение по бронированию уже есть");
            }
            if (approved) {
                booking.setStatus(Status.APPROVED);
            } else {
                booking.setStatus(Status.REJECTED);
            }
        } else {
            if (booking.getStatus().equals(Status.CANCELED)) {
                throw new ValidationException("Бронирование было отменено!");
            } else {
                throw new ValidationException("Подтвердить бронирование может только владелец вещи!");
            }
        }
        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto getBookingById(Long bookingId, Long userId) {
        userExistOrElseThrow(userRepository.findById(userId), userId);
        Booking booking = getBookingOrElseThrow(
                bookingRepository.findById(bookingId),
                bookingId
        );
        if (booking.getBooker().getId().equals(userId) ||
                booking.getItem().getOwner().getId().equals(userId)) {
            return BookingMapper.toBookingDto(booking);
        } else {
            throw new UserNotFoundException(userId);
        }
    }

    @Override
    public List<BookingDto> getBookings(String state, Long userId) {
        userExistOrElseThrow(userRepository.findById(userId), userId);
        Sort sortByStartDesc = Sort.by(Sort.Direction.DESC, "start");
        List<Booking> bookings;
        switch (state) {
            case "ALL":
                bookings = bookingRepository.findByBookerId(userId, sortByStartDesc);
                break;
            case "CURRENT":
                bookings = bookingRepository.findByBookerIdAndStartIsBeforeAndEndIsAfter(
                        userId,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        sortByStartDesc
                );
                break;
            case "PAST":
                bookings = bookingRepository.findByBookerIdAndStartIsBeforeAndEndIsBefore(
                        userId,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        sortByStartDesc
                );
                break;
            case "FUTURE":
                bookings = bookingRepository.findByBookerIdAndStartIsAfter(
                        userId,
                        LocalDateTime.now(),
                        sortByStartDesc
                );
                break;
            case "WAITING":
                bookings = bookingRepository.findByBookerIdAndStatus(
                        userId,
                        Status.WAITING,
                        sortByStartDesc
                );
                break;
            case "REJECTED":
                bookings = bookingRepository.findByBookerIdAndStatus(
                        userId,
                        Status.REJECTED,
                        sortByStartDesc
                );
                break;
            default:
                throw new ValidationException(
                        "Unknown state: " + state
                );
        }
        return bookings
                .stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getBookingsOwner(String state, Long ownerId) {
        userExistOrElseThrow(userRepository.findById(ownerId), ownerId);
        Sort sortByStartDesc = Sort.by(Sort.Direction.DESC, "start");
        List<Booking> bookings;
        switch (state) {
            case "ALL":
                bookings = bookingRepository.findByItem_Owner_id(
                        ownerId,
                        sortByStartDesc
                );
                break;
            case "CURRENT":
                bookings = bookingRepository.findByItem_Owner_IdAndStartIsBeforeAndEndIsAfter(
                        ownerId,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        sortByStartDesc
                );
                break;
            case "PAST":
                bookings = bookingRepository.findByItem_Owner_IdAndEndIsBefore(
                        ownerId,
                        LocalDateTime.now(),
                        sortByStartDesc
                );
                break;
            case "FUTURE":
                bookings = bookingRepository.findByItem_Owner_IdAndStartIsAfter(
                        ownerId,
                        LocalDateTime.now(),
                        sortByStartDesc
                );
                break;
            case "WAITING":
                bookings = bookingRepository.findByItem_Owner_IdAndStatus(
                        ownerId,
                        Status.WAITING,
                        sortByStartDesc
                );
                break;
            case "REJECTED":
                bookings = bookingRepository.findByItem_Owner_IdAndStatus(
                        ownerId,
                        Status.REJECTED,
                        sortByStartDesc
                );
                break;
            default:
                throw new ValidationException(
                        "Unknown state: " + state
                );
        }
        return bookings.
                stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    private Booking getBookingOrElseThrow(Optional<Booking> optionalBooking,
                                          Long bookingId) {
        return optionalBooking.orElseThrow(
                () -> new BookingNotFoundException(bookingId)
        );
    }

    private User getUserOrElseThrow(Optional<User> optionalUser, Long userId) {
        return optionalUser.orElseThrow(
                () -> new UserNotFoundException(userId)
        );
    }

    private Item getAvailableItemOrElseThrow(Optional<Item> optionalItem,
                                             Long itemId) {
        Item item = optionalItem.orElseThrow(
                () -> new ItemNotFoundException(itemId)
        );
        if (item.getAvailable()) {
            return item;
        }
        throw new ValidationException(String.format(
                "Вещь с id = %d недоступна для бронирования",
                itemId
        ));
    }

    private void userExistOrElseThrow(Optional<User> optionalUser, Long userId) {
        optionalUser.orElseThrow(
                () -> new UserNotFoundException(userId)
        );
    }

    private Boolean userIsOwnerOfItem(Item item, Long userId) {
        return item.getOwner().getId().equals(userId);
    }
}
