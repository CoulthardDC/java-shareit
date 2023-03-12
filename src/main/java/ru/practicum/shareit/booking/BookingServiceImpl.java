package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
    private final BookingMapper bookingMapper;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository,
                              UserRepository userRepository,
                              ItemRepository itemRepository,
                              BookingMapper bookingMapper) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.bookingMapper = bookingMapper;
    }

    @Override
    public BookingDto addBooking(BookingInputDto bookingDto, Long userId) {
        User user = getUserOrElseThrow(userRepository.findById(userId), userId);
        Item item = getAvailableItemOrElseThrow(
                itemRepository.findById(bookingDto.getItemId()),
                bookingDto.getItemId()
        );
        Booking booking = bookingMapper.toBooking(bookingDto, user, item);
        booking.setStatus(Status.WAITING);
        if (userId.equals(booking.getItem().getOwner().getId())) {
            throw new PermissionException("Вещь с ID=" + bookingDto.getItemId() +
                    " недоступна для бронирования самим владельцем!");
        }
        if (booking.getStart().isAfter(booking.getEnd())) {
            throw new ValidationException("Время начала аренды позже времени окончания");
        }
        return bookingMapper.toBookingDto(bookingRepository.save(booking));
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
        return bookingMapper.toBookingDto(bookingRepository.save(booking));
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
            return bookingMapper.toBookingDto(booking);
        } else {
            throw new UserNotFoundException(userId);
        }
    }

    @Override
    public List<BookingDto> getBookings(String state, Long userId, Integer from, Integer size) {
        userExistOrElseThrow(userRepository.findById(userId), userId);
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        PageRequest pageRequest = PageRequest.of(from / size, size, sort);
        List<Booking> bookings;
        switch (state) {
            case "ALL":
                bookings = bookingRepository.findByBookerId(userId, pageRequest);
                break;
            case "CURRENT":
                    bookings = bookingRepository.findByBookerIdAndStartIsBeforeAndEndIsAfter(
                        userId,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        pageRequest
                );
                break;
            case "PAST":
                bookings = bookingRepository.findByBookerIdAndStartIsBeforeAndEndIsBefore(
                        userId,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        pageRequest
                );
                break;
            case "FUTURE":
                bookings = bookingRepository.findByBookerIdAndStartIsAfter(
                        userId,
                        LocalDateTime.now(),
                        pageRequest
                );
                break;
            case "WAITING":
                bookings = bookingRepository.findByBookerIdAndStatus(
                        userId,
                        Status.WAITING,
                        pageRequest
                );
                break;
            case "REJECTED":
                bookings = bookingRepository.findByBookerIdAndStatus(
                        userId,
                        Status.REJECTED,
                        pageRequest
                );
                break;
            default:
                throw new ValidationException(
                        "Unknown state: " + state
                );
        }
        return bookings
                .stream()
                .map(bookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getBookingsOwner(String state, Long ownerId, Integer from, Integer size) {
        userExistOrElseThrow(userRepository.findById(ownerId), ownerId);
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        PageRequest pageRequest = PageRequest.of(from / size, size, sort);
        List<Booking> bookings;
        switch (state) {
            case "ALL":
                bookings = bookingRepository.findByItem_Owner_id(
                        ownerId,
                        pageRequest
                );
                break;
            case "CURRENT":
                bookings = bookingRepository.findByItem_Owner_IdAndStartIsBeforeAndEndIsAfter(
                        ownerId,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        pageRequest
                );
                break;
            case "PAST":
                bookings = bookingRepository.findByItem_Owner_IdAndEndIsBefore(
                        ownerId,
                        LocalDateTime.now(),
                        pageRequest
                );
                break;
            case "FUTURE":
                bookings = bookingRepository.findByItem_Owner_IdAndStartIsAfter(
                        ownerId,
                        LocalDateTime.now(),
                        pageRequest
                );
                break;
            case "WAITING":
                bookings = bookingRepository.findByItem_Owner_IdAndStatus(
                        ownerId,
                        Status.WAITING,
                        pageRequest
                );
                break;
            case "REJECTED":
                bookings = bookingRepository.findByItem_Owner_IdAndStatus(
                        ownerId,
                        Status.REJECTED,
                        pageRequest
                );
                break;
            default:
                throw new ValidationException(
                        "Unknown state: " + state
                );
        }
        return bookings
                .stream()
                .map(bookingMapper::toBookingDto)
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
