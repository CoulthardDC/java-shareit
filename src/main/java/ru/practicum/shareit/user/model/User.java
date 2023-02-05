package ru.practicum.shareit.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Модель пользователя")
public class User {
    @Schema(description = "Идентификатор пользователя", example = "1")
    Long id;

    @Email
    @NotBlank
    @Schema(description = "Емейл пользователя", example = "user@user.com")
    String email;

    @Schema(description = "Имя пользователя", example = "Coulthard_dc")
    String name;
}

