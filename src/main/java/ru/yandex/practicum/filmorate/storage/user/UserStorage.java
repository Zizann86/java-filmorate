package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    public User create(User user);

    public List<User> getAll();

    public User update(User user);

    public Optional<User> getById(Long id);
}
