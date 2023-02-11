package ru.practicum.shareit.request.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Модель запроса на вещь")
public class ItemRequest {
    @Schema(description = "Идентификатор запроса", example = "1")
    Long id;

    @Size(max = 200)
    @NotBlank
    @Schema(description = "Описание запроса", example = "Игровой ноутбук Acer")
    String description;

    @Schema(description = "Юзер, разместивший запрос")
    User requestor;

    @NotNull
    @Schema(description = "Дата и время создания запросаы")
    LocalDateTime created;
}
