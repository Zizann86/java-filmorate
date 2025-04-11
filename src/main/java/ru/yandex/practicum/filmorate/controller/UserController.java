package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserStorage userStorage;
    private final UserService userService;

    public UserController(UserService userService, UserStorage userStorage) {
        this.userService = userService;
        this.userStorage = userStorage;
    }

    @PostMapping
    public User create(@RequestBody @Valid User user) {
        log.info("Получен HTTP-запрос на создание пользователя: {}", user);
        User createUser = userStorage.create(user);
        log.info("Успешно обработан HTTP-запрос на создание пользователя: {}", user);
        return createUser;
    }

    @GetMapping
    public List<User> getAll() {
        log.info("Получен HTTP-запрос на получение всех пользователей");
        List<User> allUsers = userStorage.getAll();
        return allUsers;
    }

    @PutMapping
    public User update(@RequestBody @Valid User user) {
        log.info("Получен HTTP-запрос на обновление пользователя: {}", user);
        User updateUser = userStorage.update(user);
        log.info("Успешно выполнен HTTP-запрос на обновление пользователя: {}", user);
        return updateUser;
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        log.info("Получен HTTP-запрос на получение пользователя по id: {}", id);
        User user = userStorage.getById(id);
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
    public List<User> getAllFriends(@PathVariable Long id) {
        log.info("Получен HTTP-запрос на получение списка имеющихся друзей у пользователя по id: {}", id);
        return userService.getAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        log.info("Получен HTTP-запрос на получение списка друзей у пользователя по id: {}, общих с пользователем по id: {}", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }

}
