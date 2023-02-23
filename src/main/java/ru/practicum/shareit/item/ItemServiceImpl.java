package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.ItemCreateException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;


    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository,
                           UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
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
        Item updateItem = ItemMapper.toItem(itemDto);
        updateItem.setOwner(owner);
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
    public ItemDto getItemById(Long itemId) {
        Item item = getItemOrElseThrow(itemRepository.findById(itemId), itemId);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> getItemsByOwner(Long ownerId) {
        return itemRepository.findByOwnerId(ownerId)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
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

    private Item createItemOrElseThrow(Optional<Item> optionalItem) {
        return optionalItem.orElseThrow(
                ItemCreateException::new
        );
    }

}