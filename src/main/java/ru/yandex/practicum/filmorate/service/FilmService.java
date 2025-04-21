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

    public Film create(Film film) {
        log.info("Получен HTTP-запрос на добавление фильма: {}", film);
        Film createFilm = filmStorage.create(film);
        log.info("Успешно обработан HTTP-запрос на добавление фильма: {}", createFilm);
        return film;
    }

    public List<Film> getAll() {
        log.info("Получен HTTP-запрос на получение всех фильмов");
        List<Film> allFilms = filmStorage.getAll();
        return allFilms;
    }

    public Film update(Film film) {
        log.info("Получен HTTP-запрос на обновление фильма: {}", film);
        Film updateFilm = filmStorage.update(film);
        log.info("Успешно выполнен HTTP-запрос на обновление фильма: {}", updateFilm);
        return updateFilm;
    }

    public Film getById(Long id) {
        log.info("Получен HTTP-запрос на получение фильма по id: {}", id);
        Film film = filmStorage.getFilmById(id);
        return film;
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
