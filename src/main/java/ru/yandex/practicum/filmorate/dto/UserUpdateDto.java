package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserUpdateDto {
    private Long id;

    @Email(message = "Некорректная электронная почта")
    private String email;

    @Pattern(regexp = "\\S+", message = "Логин не должен содержать пробелы")
    @NotEmpty(message = "Логин не может быть пустым")
    private String login;

    private String name;

    @Past(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;

    public boolean hasLogin() {
        return login != null && !login.isBlank();
    }

    public boolean hasEmail() {
        return email != null && !email.isBlank();
    }

    public boolean hasName() {
        return name != null && !name.isBlank();
    }


    public boolean hasBirthday() {
        return birthday != null;
    }
}
