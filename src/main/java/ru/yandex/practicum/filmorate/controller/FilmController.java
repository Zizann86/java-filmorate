package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private Map<Long, Film> idToFilm = new HashMap<>();
    private Long idCounterFilm = 1L;

    @PostMapping
    public Film create(@RequestBody @Valid Film film) {
        log.info("Получен HTTP-запрос на добавление фильма: {}", film);
        film.setId(idCounterFilm++);
        idToFilm.put(film.getId(), film);
        log.info("Успешно обработан HTTP-запрос на добавление фильма: {}", film);
        return film;
    }

    @GetMapping
    public List<Film> getAll() {
        log.info("Получен HTTP-запрос на получение всех фильмов");
        return new ArrayList<>(idToFilm.values());
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        log.info("Получен HTTP-запрос на обновление фильма: {}", film);
        Long id = film.getId();
        if (!idToFilm.containsKey(id)) {
            String errorMessage = String.format("Фильм с id %d не найден", id);
            log.error(errorMessage);
            throw new UserNotFoundException(errorMessage);
        }
        idToFilm.put(film.getId(), film);
        log.info("Успешно выполнен HTTP-запрос на обновление фильма: {}", film);
        return film;
    }
}
