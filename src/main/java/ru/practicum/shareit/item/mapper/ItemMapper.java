package ru.practicum.shareit.item.mapper;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserMapper;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .description(item.getDescription())
                .name(item.getName())
                .owner(UserMapper.toUserDto(item.getOwner()))
                .available(item.getAvailable())
                .build();
    }

    public static Item toItem(ItemDto itemDto) {
        return Item.builder()
                .id(itemDto.getId())
                .description(itemDto.getDescription())
                .name(itemDto.getName())
                .owner(UserMapper.toUser(itemDto.getOwner()))
                .available(itemDto.getAvailable())
                .build();
    }
}