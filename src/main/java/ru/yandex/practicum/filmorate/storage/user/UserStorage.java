package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    public User create(User user);

    public List<User> getAll();

    public User update(User user);

    public User getById(Long id);
}
