package ru.practicum.shareit.booking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingShortDto {
    @Schema(description = "Идентификатор брони", example = "1")
    Long id;

    @Schema(description = "Идентификатор пользователя, открывающего бронь", example = "2")
    Long bookerId;

    @Schema(description = "Дата начала брони")
    LocalDateTime start;

    @Schema(description = "Дата окончания брони")
    LocalDateTime end;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookerId() {
        return bookerId;
    }

    public void setBookerId(Long bookerId) {
        this.bookerId = bookerId;
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
}
