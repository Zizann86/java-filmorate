package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dto.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmRepository filmRepository;
    private final FilmMapper filmMapper;

    public FilmService(@Autowired FilmRepository filmRepository, @Autowired UserService userService, @Autowired FilmMapper filmMapper) {
        this.filmRepository = filmRepository;
        this.filmMapper = filmMapper;
    }

    public FilmResponseDto create(FilmCreateDto film) {
        log.info("Получен HTTP-запрос на добавление фильма: {}", film);
        if (!filmRepository.existsByRatingId(film.getMpa().getId())) {
            throw new NotFoundException("Рейтинг с ID " + film.getMpa().getId() + " не существует.");
        }
        for (GenreDto genre : film.getGenres()) {
            if (!filmRepository.genreExists(genre.getId())) {
                throw new NotFoundException("Жанр с ID " + genre.getId() + " не существует.");
            }
        }
        Film filmToCreate = filmMapper.toFilm(film);
        Film createFilm = filmRepository.create(filmToCreate);
        FilmResponseDto responseDto = filmMapper.toResponseDto(createFilm);
        log.info("Успешно обработан HTTP-запрос на добавление фильма: {}", createFilm);
        return responseDto;
    }

    public List<FilmResponseDto> getAll() {
        log.info("Получен HTTP-запрос на получение всех фильмов");
        List<Film> allFilms = filmRepository.getAll();
        for (Film film : allFilms) {
            Set<Genre> genres = filmRepository.getGenresByFilmId(film.getId());
            film.setGenres(genres);
        }
        return allFilms.stream()
                .map(filmMapper::toResponseDto)
                .toList();
    }

    public FilmResponseDto update(FilmUpdateDto request) {
        Long filmId = request.getId();
        log.info("Получен HTTP-запрос на обновление фильма с ID: {}", filmId);
        Film existingFilm = filmRepository.getFilmById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм с ID " + filmId + " не найден"));
        filmMapper.updateFilmFields(existingFilm, request);
        existingFilm = filmRepository.update(existingFilm);
        log.info("Успешно выполнен HTTP-запрос на обновление фильма: {}", existingFilm);
        return filmMapper.toResponseDto(existingFilm);
    }

    public FilmResponseDto getById(Long id) {
        log.info("Получен HTTP-запрос на получение фильма по id: {}", id);
        Film film = filmRepository.getFilmById(id)
                .orElseThrow(() -> new NotFoundException("Фильм с id " + id + " не найден"));
        Set<Genre> genres = filmRepository.getGenresByFilmId(film.getId());
        film.setGenres(genres);
        FilmResponseDto responseDto = filmMapper.toResponseDto(film);
        log.info("Фильм успешно найден: {}", responseDto);
        return responseDto;
    }

    public void addLike(Long filmId, Long userId) {
        Optional<Film> optionalFilm = filmRepository.getFilmById(filmId);
        Film film = optionalFilm.orElseThrow(() -> new NotFoundException("Такого фильма не существует"));
        if (userId != null) {
            filmRepository.addLike(filmId, userId);
            log.info("Лайк к фильму успешно добавлен: {}", film);
        }
    }

    public void deleteLike(Long filmId, Long userId) {
        Optional<Film> optionalFilm = filmRepository.getFilmById(filmId);
        Film film = optionalFilm.orElseThrow(() -> new NotFoundException("Такого фильма не существует"));
        if (userId != null) {
            film.getLikes().remove(userId);
            log.info("Лайк к фильму успешно удален {}", film);
        }
    }

    public List<FilmResponseDto> mostPopularFilms(int count) {
        log.info("Выводим {} самых популярных фильмов", count);
        List<Film> list = filmRepository.getTopFilms(count).stream()
                .collect(Collectors.toList());
        return list.stream()
                .map(filmMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}
