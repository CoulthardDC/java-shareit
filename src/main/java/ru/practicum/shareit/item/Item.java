package ru.practicum.shareit.item;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Модель вещи")
@Entity
@Table(name = "items")
public class Item {
    @Schema(description = "Идентификатор вещи", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    Long id;

    @NotBlank
    @Schema(description = "Наименование вещи", example = "Ноутбук Acer")
    @Column(name = "item_name")
    String name;

    @Size(max = 200)
    @NotEmpty
    @Schema(description = "Описание вещи",
            example = "Ноутбук Acer. Два ядра, два гига, игровая видеокарта")
    @Column(name = "item_description")
    String description;

    @NotNull
    @Schema(description = "Владелец вещи")
    @ManyToOne
    @JoinColumn(name = "item_owner_id")
    User owner;

    @NotNull
    @Schema(description = "Доступность вещи")
    @Column(name = "item_available")
    Boolean available;

    @Schema(description = "Запрос на вещь")
    @Column(name = "item_request_id")
    Long requestId;

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
                ", request=" + requestId +
                '}';
    }
}


