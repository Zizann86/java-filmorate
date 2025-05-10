package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    public Film create(Film film);

    public List<Film> getAll();

    public Film update(Film film);

    public Optional<Film> getFilmById(Long id);
}
