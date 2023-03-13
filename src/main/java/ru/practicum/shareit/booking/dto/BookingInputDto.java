package ru.practicum.shareit.booking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Представление брони для ввода")
public class BookingInputDto {

    @Schema(description = "Идентификатор брони", example = "1")
    Long itemId;

    @FutureOrPresent
    @Schema(description = "Дата начала брони")
    @NotNull
    LocalDateTime start;

    @Future
    @Schema(description = "Дата окончания брони")
    @NotNull
    LocalDateTime end;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
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
