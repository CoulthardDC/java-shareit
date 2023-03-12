package ru.practicum.shareit.booking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.Objects;

@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Представление бронирования")
public class BookingDto {
    @Schema(description = "Идентификатор брони", example = "1")
    Long id;

    @Schema(description = "Дата и время начала брони")
    LocalDateTime start;

    @Schema(description = "Дата и время окончания брони")
    LocalDateTime end;

    @Schema(description = "Представление вещи для бронирования")
    ItemDto item;

    @Schema(description = "Представление пользователя, открывающего бронь")
    UserDto booker;

    @Schema(description = "Статус брони")
    Status status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public ItemDto getItem() {
        return item;
    }

    public void setItem(ItemDto item) {
        this.item = item;
    }

    public UserDto getBooker() {
        return booker;
    }

    public void setBooker(UserDto booker) {
        this.booker = booker;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingDto that = (BookingDto) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


}
