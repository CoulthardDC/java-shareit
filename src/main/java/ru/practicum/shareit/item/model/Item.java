package ru.practicum.shareit.item.model;

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

@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Schema(description = "Модель вещи")
public class Item {
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

    @NotNull
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

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", owner=" + owner +
                ", available=" + available +
                ", request=" + request +
                '}';
    }
}
