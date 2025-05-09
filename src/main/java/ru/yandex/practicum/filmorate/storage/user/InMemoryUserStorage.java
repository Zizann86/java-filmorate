package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private Map<Long, User> idToUser = new HashMap<>();
    private Long idCounter = 1L;

    @Override
    public User create(User user) {
        user.getName();
        user.setId(idCounter++);
        idToUser.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(idToUser.values());
    }


    @Override
    public User update(User user) {
        Long id = user.getId();
        if (!idToUser.containsKey(id)) {
            String errorMessage = String.format("Пользователь с id %d не найден", id);
            log.error(errorMessage);
            throw new UserNotFoundException(errorMessage);
        }
        idToUser.put(user.getId(), user);
        log.info("Успешно выполнен HTTP-запрос на обновление пользователя: {}", user);
        return user;
    }

    @Override
    public Optional<User> getById(Long id) {
        return idToUser.values().stream()
                .filter(user -> Objects.equals(user.getId(), id))
                .findFirst();
    }
}
