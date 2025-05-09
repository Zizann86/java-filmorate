package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private Map<Long, Film> idToFilm = new HashMap<>();
    private Long idCounterFilm = 1L;

    @Override
    public Film create(Film film) {
        log.info("Получен HTTP-запрос на добавление фильма: {}", film);
        film.setId(idCounterFilm++);
        idToFilm.put(film.getId(), film);
        log.info("Успешно обработан HTTP-запрос на добавление фильма: {}", film);
        return film;
    }

    @Override
    public List<Film> getAll() {
        log.info("Получен HTTP-запрос на получение всех фильмов");
        return new ArrayList<>(idToFilm.values());
    }

    @Override
    public Film update(Film film) {
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

    public Optional<Film> getFilmById(Long id) {
        return idToFilm.values().stream()
                .filter(film -> Objects.equals(film.getId(), id))
                .findFirst();
    }
}
