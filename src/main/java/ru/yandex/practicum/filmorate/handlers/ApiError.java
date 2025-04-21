package ru.yandex.practicum.filmorate.handlers;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiError {
    String description;
    Integer errorCode;

}
