package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {
    Long id;                // id вещи

    @NotBlank
    String name;            // название вещи

    @Size(max = 200)
    @NotEmpty
    String description;     // описание вещи

    User owner;             // владелец вещи

    @NotNull
    Boolean available;      // статус доступности вещи

    ItemRequest request;    // ссылка на запрос
}
