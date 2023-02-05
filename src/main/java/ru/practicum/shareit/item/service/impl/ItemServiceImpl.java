package ru.practicum.shareit.item.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.ItemCreateException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemDao itemDao;
    private final UserDao userDao;

    @Autowired
    public ItemServiceImpl(ItemDao itemDao, UserDao userDao) {
        this.itemDao = itemDao;
        this.userDao = userDao;
    }

    @Override
    public ItemDto addItem(ItemDto itemDto, Long ownerId) {
        User owner = getUserOrElseThrow(userDao.findUserById(ownerId), ownerId);
        Item item = createItemOrElseThrow(itemDao.save(ItemMapper.toItem(itemDto)));
        item.setOwner(owner);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, Long itemId, Long ownerId) {
        itemDto.setId(itemId);
        Item item = createItemOrElseThrow(itemDao.update(ItemMapper.toItem(itemDto), ownerId));
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        Item item = getItemOrElseThrow(itemDao.findItemById(itemId), itemId);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> getItemsByOwner(Long ownerId) {
        return itemDao.findItemsByOwner(ownerId)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getItemsBySearch(String text) {
        return itemDao.findItemsBySearch(text.toLowerCase())
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
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
