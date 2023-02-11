package ru.practicum.shareit.item.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getOwner() {
        return owner;
    }

    public Boolean getAvailable() {
        return available;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemDto itemDto = (ItemDto) o;
        return Objects.equals(id, itemDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
