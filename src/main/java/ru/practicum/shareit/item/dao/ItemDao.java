package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemDao {

    Optional<Item> save(Item item);

    Optional<Item> update(Item item, Long ownerId);

    Optional<Item> findItemById(Long itemId);

    List<Item> findItemsByOwner(Long ownerId);
}
