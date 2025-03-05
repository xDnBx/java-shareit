package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    @NotBlank(message = "Имя пользователя не может быть пустым")
    String name;

    @Email(message = "Поле должно содержать символ @")
    @NotBlank(message = "E-mail не может быть пустым")
    String email;
}