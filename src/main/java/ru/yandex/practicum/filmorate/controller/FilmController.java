package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmCreateDto;
import ru.yandex.practicum.filmorate.dto.FilmResponseDto;
import ru.yandex.practicum.filmorate.dto.FilmUpdateDto;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    public FilmController(@Autowired FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public FilmResponseDto create(@RequestBody @Valid FilmCreateDto film) {
        log.info("Получен HTTP-запрос на добавление фильма: {}", film);
        FilmResponseDto createFilm = filmService.create(film);
        log.info("Успешно обработан HTTP-запрос на добавление фильма: {}", createFilm);
        return createFilm;
    }

    @GetMapping
    public List<FilmResponseDto> getAll() {
        log.info("Получен HTTP-запрос на получение всех фильмов");
        List<FilmResponseDto> allFilms = filmService.getAll();
        return allFilms;
    }

    @PutMapping
    public FilmResponseDto update(@RequestBody @Valid FilmUpdateDto film) {
        log.info("Получен HTTP-запрос на обновление фильма");
        FilmResponseDto updatedFilm = filmService.update(film);
        log.info("Успешно выполнен HTTP-запрос на обновление фильма");
        return updatedFilm;
    }

    @GetMapping("/{id}")
    public FilmResponseDto getById(@PathVariable Long id) {
        log.info("Получен HTTP-запрос на получение фильма по id: {}", id);
        FilmResponseDto film = filmService.getById(id);
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
    public List<FilmResponseDto> getPopular(@RequestParam(defaultValue = "10")  int count) {
        return filmService.mostPopularFilms(count);
    }
}
