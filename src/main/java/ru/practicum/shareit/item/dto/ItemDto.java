package ru.practicum.shareit.item.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Сущность вещи")
public class ItemDto {
    @Schema(description = "Идентификатор вещи", example = "1")
    Long id;

    @NotBlank
    @Schema(description = "Наименование вещи", example = "Ноутбук Acer")
    String name;

    @Size(max = 200)
    @NotEmpty
    @Schema(description = "Описание вещи",
            example = "Ноутбук Acer. Два ядра, два гига, игровая видеокарта")
    String description;

    @Schema(description = "Владелец вещи")
    User owner;

    @NotNull
    @Schema(description = "Доступность вещи")
    Boolean available;

    @Schema(description = "Запрос на вещь")
    ItemRequest request;
}
