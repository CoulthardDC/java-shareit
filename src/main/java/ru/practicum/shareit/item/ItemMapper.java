package ru.practicum.shareit.item;
import ru.practicum.shareit.item.dto.ItemDto;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .description(item.getDescription())
                .name(item.getName())
                .owner(item.getOwner())
                .available(item.getAvailable())
                .build();
    }

    public static Item toItem(ItemDto itemDto) {
        return Item.builder()
                .id(itemDto.getId())
                .description(itemDto.getDescription())
                .name(itemDto.getName())
                .owner(itemDto.getOwner())
                .available(itemDto.getAvailable())
                .build();
    }
}