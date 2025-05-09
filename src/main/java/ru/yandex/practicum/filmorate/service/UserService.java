package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dto.UserCreateDto;
import ru.yandex.practicum.filmorate.dto.UserResponseDto;
import ru.yandex.practicum.filmorate.dto.UserUpdateDto;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(@Autowired UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public void addFriend(long userId, long friendId) {
        if (!userRepository.doesUserExist(userId)) {
            log.warn("Пользователь с id={} не найден.", userId);
            throw new UserNotFoundException("Пользователь с id=" + userId + " не найден.");
        }
        if (!userRepository.doesUserExist(friendId)) {
            log.warn("Пользователь с id={} не найден.", friendId);
            throw new UserNotFoundException("Пользователь с id=" + friendId + " не найден.");
        }
        if (userRepository.isTestingfriendship(userId, friendId)) {
            log.warn("Пользователи с id={} и id={} уже являются друзьями.", userId, friendId);
            return;
        }
        userRepository.addFriend(userId, friendId);
        log.info("Пользователь с ID {} добавил друга с ID {}", userId, friendId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        if (!userExists(userId)) {
            log.warn("Пользователь с ID {} не найден.", userId);
            throw new UserNotFoundException("Пользователь с ID " + userId + " не найден.");
        }
        if (!userExists(friendId)) {
            log.warn("Друг с ID {} не найден.", friendId);
            throw new UserNotFoundException("Друг с ID " + friendId + " не найден.");
        }
        userRepository.removeFriend(userId, friendId);
        log.info("Пользователь с ID {} удалил друга с ID {}", userId, friendId);
    }

    public List<UserResponseDto> getAllFriends(Long userId) {
        Collection<User> friends = userRepository.getUserFriends(userId);
        return friends.stream()
                .map(userMapper::toUseResponseDto)
                .collect(Collectors.toList());
    }

    public List<UserResponseDto> getCommonFriends(Long id, Long friendId) {
        User user = userRepository.getById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с ID " + id + " не найден"));
        User otherUser = userRepository.getById(friendId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с ID " + friendId + " не найден"));
        Set<Long> userFriends = user.getFriends();
        Set<Long> otherUserFriends = otherUser.getFriends();
        List<Long> commonFriendIds = userFriends.stream()
                .filter(otherUserFriends::contains)
                .collect(Collectors.toList());
        List<User> commonFriends = userRepository.getCommonFriends(user.getId(), otherUser .getId());
        List<UserResponseDto> commonFriendsDto = commonFriends.stream()
                .map(userMapper::toUseResponseDto)
                .collect(Collectors.toList());
        log.info("Список общих друзей пользователей с ID {} и {}: {}", id, friendId, commonFriendsDto);
        return commonFriendsDto;
    }

    public UserResponseDto getUserById(Long userId) {
        User user = userRepository.getById(userId)
                .orElseThrow(() -> new UserNotFoundException("Такого пользователя не существует"));
        return userMapper.toUseResponseDto(user);
    }

    public UserResponseDto create(UserCreateDto user) {
        log.info("Получен HTTP-запрос на создание пользователя: {}", user);
        User userToCreate = userMapper.mapToUser(user);
        User createdUser = userRepository.create(userToCreate);
        log.info("Успешно обработан HTTP-запрос на создание пользователя: {}", user);
        return userMapper.toUseResponseDto(createdUser);
    }

    public List<UserResponseDto> getAll() {
        log.info("Получен HTTP-запрос на получение всех пользователей");
        List<User> allUsers = userRepository.getAll();
        return allUsers.stream()
                .map(userMapper::toUseResponseDto)
                .toList();
    }

    public UserResponseDto update(UserUpdateDto request) {
        Long userId = request.getId();
        log.info("Получен HTTP-запрос на обновление пользователя с ID: {}", userId);
        User existingUser = userRepository.getById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с ID " + userId + " не найден"));
        User updatedUser = UserMapper.updateUserFields(request, existingUser);
        updatedUser = userRepository.update(updatedUser);
        log.info("Успешно выполнен HTTP-запрос на обновление пользователя: {}", updatedUser);
        return userMapper.toUseResponseDto(updatedUser);
    }

    private boolean userExists(Long userId) {
        return userRepository.getById(userId).isPresent();
    }
}
