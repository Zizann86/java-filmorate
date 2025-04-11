package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmStorage filmStorage;
    private final FilmService filmService;

    public FilmController(@Autowired FilmService filmService, @Autowired FilmStorage filmStorage) {
        this.filmService = filmService;
        this.filmStorage = filmStorage;
    }

    @PostMapping
    public Film create(@RequestBody @Valid Film film) {
        log.info("Получен HTTP-запрос на добавление фильма: {}", film);
        Film createFilm = filmStorage.create(film);
        log.info("Успешно обработан HTTP-запрос на добавление фильма: {}", createFilm);
        return film;
    }

    @GetMapping
    public List<Film> getAll() {
        log.info("Получен HTTP-запрос на получение всех фильмов");
        List<Film> allFilms = filmStorage.getAll();
        return allFilms;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        log.info("Получен HTTP-запрос на обновление фильма: {}", film);
        Film updateFilm = filmStorage.update(film);
        log.info("Успешно выполнен HTTP-запрос на обновление фильма: {}", updateFilm);
        return updateFilm;
    }

    @GetMapping("/{id}")
    public Film getById(@PathVariable Long id) {
        log.info("Получен HTTP-запрос на получение фильма по id: {}", id);
        Film film = filmStorage.getFilmById(id);
        return film;
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Получен HTTP-запрос: пользователь с id {} ставит лайк фильму id {}", userId, id);
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Получен HTTP-запрос: пользователь с id {} удаляет лайк id {}", userId, id);
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(defaultValue = "10") int size) {
        return filmService.mostPopularFilms(size);
    }
}
