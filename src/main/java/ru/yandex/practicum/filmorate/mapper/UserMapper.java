package ru.yandex.practicum.filmorate.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.UserCreateDto;
import ru.yandex.practicum.filmorate.dto.UserResponseDto;
import ru.yandex.practicum.filmorate.dto.UserUpdateDto;
import ru.yandex.practicum.filmorate.model.User;

@Component
public class UserMapper {

    public User mapToUser(UserCreateDto request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setLogin(request.getLogin());
        user.setName(request.getName());
        user.setBirthday(request.getBirthday());
        return user;
    }

    public UserResponseDto toUseResponseDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getFriends(),
                user.getFriendships()
        );
    }

    public static User updateUserFields(UserUpdateDto request, User existingUser ) {
        if (request.hasLogin()) {
            existingUser .setLogin(request.getLogin());
        }
        if (request.hasEmail()) {
            existingUser .setEmail(request.getEmail());
        }
        if (request.hasName()) {
            existingUser .setName(request.getName());
        }
        if (request.hasBirthday()) {
            existingUser .setBirthday(request.getBirthday());
        }
        return existingUser ;
    }
}
