package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;


    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository,
                           UserRepository userRepository,
                           BookingRepository bookingRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public void removeItem(Long itemId, Long ownerId) {
        Item item = getItemOrElseThrow(itemRepository.findById(itemId),
                itemId);
        if (!item.getOwner().getId().equals(ownerId)) {
            throw new RuntimeException("Нет прав на удаление");
        }
        itemRepository.deleteById(itemId);
    }
    @Override
    public ItemDto addItem(ItemDto itemDto, Long ownerId) {
        User owner = getUserOrElseThrow(userRepository.findById(ownerId), ownerId);
        itemDto.setOwner(UserMapper.toUserDto(owner));
        Item updateItem = ItemMapper.toItem(itemDto);
        Item item = itemRepository.save(updateItem);

        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, Long itemId, Long ownerId) {
        Item item = getItemOrElseThrow(
                itemRepository.findById(itemId),
                itemId);
        if (!item.getOwner().getId().equals(ownerId)) {
            throw new ItemNotFoundException(itemId);
        }
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto getItemById(Long itemId, Long userId) {
        Item item = getItemOrElseThrow(itemRepository.findById(itemId), itemId);
        ItemDto itemDto = ItemMapper.toItemDto(item);
        if (userId.equals(item.getOwner().getId())) {
            setBookingForItemDto(itemDto);
        }
        return itemDto;
    }

    @Override
    public List<ItemDto> getItemsByOwner(Long ownerId) {
        List<ItemDto> items = itemRepository.findByOwnerId(ownerId)
                .stream()
                .map(ItemMapper::toItemDto)
                .sorted(Comparator.comparing(ItemDto::getId))
                .collect(Collectors.toList());
        items.forEach(this::setBookingForItemDto);
        return items;
    }

    @Override
    public List<ItemDto> getItemsBySearch(String text) {
        if ((text != null) && (!text.isEmpty()) && (!text.isBlank())) {
            text = text.toLowerCase();
            return itemRepository.getItemsBySearchQuery(text)
                    .stream()
                    .map(ItemMapper::toItemDto)
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    private Item getItemOrElseThrow(Optional<Item> optionalItem, Long itemId) {
        return optionalItem.orElseThrow(
                () -> new ItemNotFoundException(itemId)
        );
    }

    private User getUserOrElseThrow(Optional<User> optionalUser, Long userId) {
        return optionalUser.orElseThrow(
                () -> new UserNotFoundException(userId)
        );
    }

    private void setBookingForItemDto(ItemDto itemDto) {
        Booking lastBooking = bookingRepository.findFirstByItem_IdAndEndBeforeOrderByEndDesc(
                itemDto.getId(),
                LocalDateTime.now()
        );
        BookingShortDto lastBookingDto;
        if (lastBooking == null) {
            lastBookingDto = null;
        } else {
            lastBookingDto = BookingMapper.toBookingShortDto(lastBooking);
        }

        Booking nextBooking = bookingRepository.findFirstByItem_IdAndStartAfterOrderByStartAsc(
                itemDto.getId(),
                LocalDateTime.now()
        );
        BookingShortDto nextBookingDto;
        if (nextBooking == null) {
            nextBookingDto = null;
        } else {
            nextBookingDto = BookingMapper.toBookingShortDto(nextBooking);
        }
        itemDto.setLastBooking(lastBookingDto);
        itemDto.setNextBooking(nextBookingDto);
    }

}