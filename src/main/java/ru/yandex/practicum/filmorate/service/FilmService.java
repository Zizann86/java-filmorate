package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final UserService userService;
    private final InMemoryFilmStorage filmStorage;

    public FilmService(@Autowired InMemoryFilmStorage filmStorage, @Autowired UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public void addLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId);
        if (film == null) {
            throw new UserNotFoundException("Такого фильма не существует");
        }
        if (userService.getUserById(userId) == null) {
            throw new UserNotFoundException("Такого пользователя не существует");
        }
        if (userId != null) {
            film.getLikes().add(userId);
            log.info("Лайк к фильму успешно добавлен: {}", film);
        }
    }

    public void deleteLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId);
        if (film == null) {
            throw new UserNotFoundException("Такого фильма не существует");
        }
        if (userService.getUserById(userId) == null) {
            throw new UserNotFoundException("Такого пользователя не существует");
        }
        if (userId != null) {
            film.getLikes().remove(userId);
            log.info("Лайк к фильму успешно удален {}", film);
        }
    }

    public List<Film> mostPopularFilms(int size) {
        log.info("Выводим {} самых популярных фильмов", size);
        return filmStorage.getAll().stream()
                .sorted((i, j) -> j.getLikes().size() - i.getLikes().size())
                .limit(size)
                .collect(Collectors.toList());
    }

}
