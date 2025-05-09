package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.UserCreateDto;
import ru.yandex.practicum.filmorate.dto.UserResponseDto;
import ru.yandex.practicum.filmorate.dto.UserUpdateDto;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

 @PostMapping
    public UserResponseDto create(@RequestBody @Valid UserCreateDto user) {
        log.info("Получен HTTP-запрос на создание пользователя: {}", user);
        UserResponseDto createdUser  = userService.create(user);
        log.info("Успешно обработан HTTP-запрос на создание пользователя: {}", createdUser);
        return createdUser;
    }

    @GetMapping
    public List<UserResponseDto> getAll() {
        log.info("Получен HTTP-запрос на получение всех пользователей");
        List<UserResponseDto> allUsers = userService.getAll();
        return allUsers;
    }

    @PutMapping
    public UserResponseDto update(@RequestBody @Valid UserUpdateDto user) {
        log.info("Получен HTTP-запрос на обновление пользователя");
        log.info("Данные для обновления: {}", user);
        UserResponseDto updatedUser  = userService.update(user);
        log.info("Успешно выполнен HTTP-запрос на обновление пользователя");
        return updatedUser;
    }

    @GetMapping("/{id}")
    public UserResponseDto getById(@PathVariable Long id) {
        log.info("Получен HTTP-запрос на получение пользователя по id: {}", id);
        UserResponseDto user = userService.getUserById(id);
        return user;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void createFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Получен HTTP-запрос на добавление пользователю с id: {} в друзья пользователя c id: {}", id, friendId);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Получен HTTP-запрос на удаление из друзей у пользователя с id: {} пользователя c id: {}", id, friendId);
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<UserResponseDto> getAllFriends(@PathVariable Long id) {
        log.info("Получен HTTP-запрос на получение списка имеющихся друзей у пользователя по id: {}", id);
        return userService.getAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<UserResponseDto> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        log.info("Получен HTTP-запрос на получение списка друзей у пользователя по id: {}, общих с пользователем по id: {}", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }
}
