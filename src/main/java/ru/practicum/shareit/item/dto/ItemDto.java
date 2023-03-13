package ru.practicum.shareit.item.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
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
    UserDto owner;

    @NotNull
    @Schema(description = "Доступность вещи")
    Boolean available;

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    @Schema(description = "Запрос на вещь")
    Long requestId;

    List<CommentDto> comments;

    public List<CommentDto> getComments() {
        return comments;
    }

    public void setComments(List<CommentDto> comments) {
        this.comments = comments;
    }

    BookingShortDto lastBooking;
    BookingShortDto nextBooking;

    public BookingShortDto getLastBooking() {
        return lastBooking;
    }

    public void setLastBooking(BookingShortDto lastBooking) {
        this.lastBooking = lastBooking;
    }

    public BookingShortDto getNextBooking() {
        return nextBooking;
    }

    public void setNextBooking(BookingShortDto nextBooking) {
        this.nextBooking = nextBooking;
    }

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

    public UserDto getOwner() {
        return owner;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setOwner(UserDto owner) {
        this.owner = owner;
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