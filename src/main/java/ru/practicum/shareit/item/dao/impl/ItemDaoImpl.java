package ru.practicum.shareit.item.dao.impl;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ItemDaoImpl implements ItemDao {
    private final Map<Long, Item> items = new HashMap<>();
    private Long id = 0L;

    @Override
    public Optional<Item> save(Item item) {
        if (item.getId() != null) {
            return Optional.empty();
        } else {
            item.setId(++id);
            items.put(item.getId(), item);
            return Optional.of(item);
        }
    }

    @Override
    public Optional<Item> update(Item item, Long ownerId) {
        if (items.containsKey(item.getId())
                && items.get(item.getId()).getOwner().getId().equals(ownerId)) {
            Item replacedItem = items.get(item.getId());
            if (item.getName() != null && !item.getName().isBlank()) {
                replacedItem.setName(item.getName());
            }
            if (item.getDescription() != null && !item.getDescription().isBlank()) {
                replacedItem.setDescription(item.getDescription());
            }
            if (item.getAvailable() != null) {
                replacedItem.setAvailable(item.getAvailable());
            }
            return Optional.of(replacedItem);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Item> findItemById(Long itemId) {
        return Optional.ofNullable(items.get(itemId));
    }

    @Override
    public List<Item> findItemsByOwner(Long ownerId) {
        return items.values()
                .stream()
                .filter(i -> i.getOwner().getId().equals(ownerId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> findItemsBySearch(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        } else {
            return items.values()
                    .stream()
                    .filter(i -> (i.getName().toLowerCase().contains(text)
                            || i.getDescription().toLowerCase().contains(text))
                            && i.getAvailable())
                    .collect(Collectors.toList());
        }
    }
}
