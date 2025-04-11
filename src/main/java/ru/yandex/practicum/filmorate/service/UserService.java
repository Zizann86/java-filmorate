package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;


    public UserService(@Autowired UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(Long userId, Long friendId) {
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);
        if (user == null || friend == null) {
            throw new UserNotFoundException("Таких пользователей не существует");
        }
        user.getFriends().add(friend.getId());
        friend.getFriends().add(user.getId());
        log.info("Пользователь добавил друга: {}", friend);
    }

    public void deleteFriend(Long userId, Long friendId) {
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);
        if (user != null && friend != null) {
            log.info("Пользователь удалил друга: {}", friend);
            user.getFriends().remove(friend.getId());
            friend.getFriends().remove(user.getId());
        } else {
            throw new UserNotFoundException("Такого пользователя не существует");
        }
    }

    public List<User> getAllFriends(Long id) {
        User user = userStorage.getById(id);
        if (user == null) {
            throw new UserNotFoundException("Такого пользователя не существует");
        }
        List<User> usersFriend = user.getFriends().stream()
                .map(userStorage::getById)
                .collect(Collectors.toList());
        log.info("Список друзей пользователя: {}", usersFriend);
        return usersFriend;
    }

    public List<User> getCommonFriends(Long id, Long friendId) {
        User user = userStorage.getById(id);
        User otherUser  = userStorage.getById(friendId);
        if (user == null) {
            throw new UserNotFoundException("Пользователь с ID " + id + " не найден");
        }
        if (otherUser  == null) {
            throw new UserNotFoundException("Пользователь с ID " + friendId + " не найден");
        }
        Set<Long> userFriends = user.getFriends();
        Set<Long> otherUserFriends = otherUser.getFriends();
        List<User> users = userFriends.stream()
                .filter(otherUserFriends::contains)
                .map(userStorage::getById)
                .collect(Collectors.toList());
        log.info("Список общих друзей пользователей: {}", users);
        return users;
    }

    public User getUserById(Long userId) {
        User user = userStorage.getById(userId);
        if (user == null) {
            throw new UserNotFoundException("Такого пользователя не существует");
        }
        return user;
    }

}
