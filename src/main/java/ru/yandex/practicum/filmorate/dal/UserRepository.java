package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class UserRepository extends BaseRepository<User> implements UserStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM users";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE user_id = ?";
    private static final String FIND_INSERT_USER =
            "INSERT INTO users (email, login, name, birthday) " +
                    "VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? " +
            "WHERE user_id = ?";
    private static final String DELETE_USER = "DELETE FROM USERS WHERE USER_ID = ?";
    private static final String ADD_FRIEND = "INSERT INTO friendships (user_id, friend_id) VALUES (?, ?)";
    private static final String DELETE_FRIEND = "DELETE FROM friendships WHERE user_id=? AND friend_id=?";
    private static final String GET_USER_FRIENDS = "SELECT u.* FROM users u " +
            "JOIN friendships f ON u.user_id = f.friend_id " +
            "WHERE f.user_id = ?";
    private static final String GET_COMMON_FRIENDS =
            "SELECT u.* FROM users u " +
                    "WHERE u.user_id IN (" +
                    "SELECT f1.friend_id " +
                    "FROM friendships f1 " +
                    "JOIN friendships f2 ON f1.friend_id = f2.friend_id " +
                    "WHERE f1.user_id = ? AND f2.user_id = ?" +
                    ")";
    private static final String CHECK_FRIENDSHIP =
            "SELECT COUNT(*) FROM friendships WHERE (user_id = ? AND friend_id = ?) OR (user_id = ? AND friend_id = ?)";
    private static final String REMOVE_ALL_FRIENDS =
            "DELETE FROM FRIENDSHIPS WHERE USER_ID = ? OR FRIEND_ID = ?;";
    private static final String GET_BY_ID = "SELECT COUNT(*) FROM users WHERE user_id = ?";

    public UserRepository(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Optional<User> getById(Long userId) {
        log.info("Запрос пользователя с ID: {}", userId);
        return findOne(FIND_BY_ID_QUERY, userId);
    }

    @Override
    public List<User> getAll() {
        List<User> users = findMany(FIND_ALL_QUERY);
        return users;
    }

    @Override
    public User create(User user) {
        long id = insert(
                FIND_INSERT_USER,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );
        user.setId(id);
        return user;
    }

    @Override
    public User update(User user) {
        try {
            update(UPDATE_QUERY,
                    user.getEmail(),
                    user.getLogin(),
                    user.getName(),
                    user.getBirthday(),
                    user.getId());
            log.info("Пользователь с ID {} успешно обновлен", user.getId());
            return user;
        } catch (Exception e) {
            log.error("Ошибка при обновлении пользователя с ID {}: {}", user.getId(), e.getMessage());
            throw new NotFoundException("Не удалось обновить данные пользователя");
        }
    }

    public void deleteUserById(long userId) {
        delete(DELETE_USER, userId);
    }

    public void addFriend(long userId, long friendId) {

        jdbc.update(ADD_FRIEND, userId, friendId);
    }

    public void removeFriend(long userId, long friendId) {
        if (!isTestingfriendship(userId, friendId)) {
            log.warn("Пользователи с id={} и id={} не являются друзьями.", userId, friendId);
            return;
        }
        jdbc.update(DELETE_FRIEND, userId, friendId);
    }

    public Collection<User> getUserFriends(long userId) {
        if (!doesUserExist(userId)) {
            log.warn("Пользователь с id={} не найден.", userId);
            throw new NotFoundException("Пользователь с id=" + userId + " не найден.");
        }
        try {
            return jdbc.query(GET_USER_FRIENDS, mapper, userId);
        } catch (DataAccessException e) {
            log.error("Ошибка при поиске друзей для userId = {}", userId, e);
            return null;
        }
    }

    public List<User> getCommonFriends(long userId, long otherId) {
        return jdbc.query(GET_COMMON_FRIENDS, mapper, userId, otherId);
    }

    public boolean doesUserExist(long userId) {
        Integer count = jdbc.queryForObject(GET_BY_ID, new Object[]{userId}, Integer.class);
        return count != null && count > 0;
    }

    public boolean isTestingfriendship(long userId, long friendId) {
        try {
            Integer count = jdbc.queryForObject(CHECK_FRIENDSHIP, Integer.class, userId, friendId, friendId, userId);
            return count != null && count > 0;
        } catch (DataAccessException e) {
            log.error("Ошибка дружбы между пользователями с id={} и id={}: {}", userId, friendId, e.getMessage());
            return false;
        }
    }
}
