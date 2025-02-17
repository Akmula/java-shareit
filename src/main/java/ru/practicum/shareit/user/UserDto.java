package ru.practicum.shareit.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Integer id;

    @NotBlank(message = "Имя не может быть пустым!")
    @Size(min = 4, max = 50, message = "Имя должно быть от 4 до 50 символов!")
    private String name;

    @NotBlank(message = "Email не может быть пустым!")
    @Email(message = "Неверный формат почты!")
    private String email;
}