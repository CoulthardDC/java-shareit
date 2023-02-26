package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.CommentCreateException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
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
    private final CommentRepository commentRepository;
    private final UserMapper userMapper;
    private final ItemMapper itemMapper;

    private final BookingMapper bookingMapper;


    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository,
                           UserRepository userRepository,
                           BookingRepository bookingRepository,
                           CommentRepository commentRepository,
                           UserMapper userMapper,
                           ItemMapper itemMapper,
                           BookingMapper bookingMapper) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
        this.userMapper = userMapper;
        this.itemMapper = itemMapper;
        this.bookingMapper = bookingMapper;
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
        itemDto.setOwner(userMapper.toUserDto(owner));
        Item updateItem = itemMapper.toItem(itemDto);
        Item item = itemRepository.save(updateItem);

        return itemMapper.toItemDto(item);
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
        return itemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto getItemById(Long itemId, Long userId) {
        Item item = getItemOrElseThrow(itemRepository.findById(itemId), itemId);
        ItemDto itemDto = itemMapper.toItemDto(item);
        if (userId.equals(item.getOwner().getId())) {
            setBookingForItemDto(itemDto);
        } else {
            itemDto.setLastBooking(null);
            itemDto.setNextBooking(null);
        }
        itemDto.setComments(
                commentRepository.findAllByItem_Id(itemId,
                                Sort.by(Sort.Direction.DESC, "created"))
                        .stream()
                        .map(CommentMapper::toCommentDto)
                        .collect(Collectors.toList())
        );
        return itemDto;
    }

    @Override
    public List<ItemDto> getItemsByOwner(Long ownerId) {
        List<ItemDto> items = itemRepository.findByOwnerId(ownerId)
                .stream()
                .map(itemMapper::toItemDto)
                .sorted(Comparator.comparing(ItemDto::getId))
                .collect(Collectors.toList());
        items.forEach(this::setBookingForItemDto);
        items.forEach(
                itemDto -> itemDto.setComments(
                        commentRepository.findAllByItem_Id(itemDto.getId(),
                                        Sort.by(Sort.Direction.DESC, "created"))
                                .stream()
                                .map(CommentMapper::toCommentDto)
                                .collect(Collectors.toList()))
        );
        return items;
    }

    @Override
    public List<ItemDto> getItemsBySearch(String text) {
        if ((text != null) && (!text.isEmpty()) && (!text.isBlank())) {
            text = text.toLowerCase();
            return itemRepository.getItemsBySearchQuery(text)
                    .stream()
                    .map(itemMapper::toItemDto)
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<CommentDto> getCommentsByItemId(Long itemId) {
        return commentRepository.findAllByItem_Id(itemId, Sort.by(Sort.Direction.DESC, "created"))
                .stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto addComment(CommentDto commentDto, Long itemId, Long userId) {
        User user = getUserOrElseThrow(userRepository.findById(userId), userId);
        Item item = getItemOrElseThrow(itemRepository.findById(itemId), itemId);
        Booking booking = bookingRepository.findFirstByItem_idAndBooker_IdAndEndBefore(
                itemId,
                userId,
                LocalDateTime.now()
        );
        if (booking != null) {
            Comment comment = Comment.builder()
                    .id(commentDto.getId())
                    .text(commentDto.getText())
                    .item(item)
                    .author(user)
                    .created(LocalDateTime.now())
                    .build();
            return CommentMapper.toCommentDto(commentRepository.save(comment));
        } else {
            throw new CommentCreateException("Пользователь не бранировал вещь");
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
            lastBookingDto = bookingMapper.toBookingShortDto(lastBooking);
        }

        Booking nextBooking = bookingRepository.findFirstByItem_IdAndStartAfterAndStatusNotOrderByStartAsc(
                itemDto.getId(),
                LocalDateTime.now(),
                Status.REJECTED
        );
        BookingShortDto nextBookingDto;
        if (nextBooking == null) {
            nextBookingDto = null;
        } else {
            nextBookingDto = bookingMapper.toBookingShortDto(nextBooking);
        }
        itemDto.setLastBooking(lastBookingDto);
        itemDto.setNextBooking(nextBookingDto);
    }

}