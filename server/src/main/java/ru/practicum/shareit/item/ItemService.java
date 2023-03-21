package ru.practicum.shareit.item;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto addItem(ItemDto itemDto, Long ownerId);

    ItemDto updateItem(ItemDto itemDto, Long itemId, Long ownerId);

    ItemDto getItemById(Long itemId, Long userId);

    List<ItemDto> getItemsByOwner(Long ownerId, Integer from, Integer size);

    List<ItemDto> getItemsBySearch(String text, Integer from, Integer size);

    void removeItem(Long itemId, Long ownerId);

    List<CommentDto> getCommentsByItemId(Long itemId);

    CommentDto addComment(CommentDto commentDto, Long itemId, Long userId);
}