package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserMapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ItemMapper {
    Item toItem (ItemDto itemDto);

    ItemDto toItemDto (Item item);
}
