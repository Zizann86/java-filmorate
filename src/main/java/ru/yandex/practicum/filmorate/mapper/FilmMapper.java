package ru.yandex.practicum.filmorate.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FilmMapper {

    private final GenreMapper genreMapper;
    private final RatingMapper ratingMapper;

    public Film toFilm(FilmCreateDto dto) {
        if (dto == null) {
            return null;
        }
        Film film = new Film();
        film.setName(dto.getName());
        film.setDescription(dto.getDescription());
        film.setReleaseDate(dto.getReleaseDate());
        film.setDuration(dto.getDuration());
        film.setLikes(dto.getLikes());
        film.setMpa(resolveMpaRating(dto.getMpa()));
        film.setGenres(resolveGenres(dto.getGenres()));
        return film;
    }

    public Film toFilm(FilmUpdateDto dto) {
        if (dto == null) {
            return null;
        }
        Film film = new Film();
        film.setId(dto.getId());
        film.setName(dto.hasName() ? dto.getName() : null);
        film.setDescription(dto.hasDescription() ? dto.getDescription() : null);
        film.setReleaseDate(dto.hasReleaseDate() ? dto.getReleaseDate() : null);
        film.setDuration(dto.hasDuration() ? dto.getDuration() : null);
        film.setLikes(dto.getLikes());
        film.setMpa(resolveMpaRating(dto.getMpa()));
        film.setGenres(resolveGenres(dto.getGenres()));
        return film;
    }

    public void updateFilmFields(Film film, FilmUpdateDto dto) {
        if (dto == null || film == null) {
            return;
        }
        if (dto.hasName()) {
            film.setName(dto.getName());
        }
        if (dto.hasDescription()) {
            film.setDescription(dto.getDescription());
        }
        if (dto.hasReleaseDate()) {
            film.setReleaseDate(dto.getReleaseDate());
        }
        if (dto.hasDuration()) {
            film.setDuration(dto.getDuration());
        }
        film.setLikes(dto.getLikes());
        film.setMpa(resolveMpaRating(dto.getMpa()));
        film.setGenres(resolveGenres(dto.getGenres()));
    }

    public FilmResponseDto toResponseDto(Film film) {
        if (film == null) {
            return null;
        }
        FilmResponseDto responseDto = new FilmResponseDto();
        responseDto.setId(film.getId());
        responseDto.setName(film.getName());
        responseDto.setDescription(film.getDescription());
        responseDto.setReleaseDate(film.getReleaseDate());
        responseDto.setDuration(film.getDuration());
        responseDto.setLikes(film.getLikes());
        responseDto.setMpa(ratingMapper.mapToRatingDto(film.getMpa()));
        if (film.getGenres() != null) {
            responseDto.setGenres(film.getGenres().stream()
                    .map(genreMapper::mapToGenreDto)
                    .collect(Collectors.toSet()));
        } else {
            responseDto.setGenres(Collections.emptySet());
        }
        return responseDto;
    }

    private Rating resolveMpaRating(RatingDto mpa) {
        if (mpa == null) {
            System.out.println("RatingDto is null");
            return null;
        }
        if (mpa.getId() <= 0) {
            System.out.println("Invalid RatingDto id: " + mpa.getId());
            return null;
        }
        return ratingMapper.mapToRating(mpa);
    }

    private Set<Genre> resolveGenres(Set<GenreDto> genres) {
        if (genres == null || genres.isEmpty()) return Set.of();
        return genres.stream()
                .filter(Objects::nonNull)
                .map(genreMapper::mapToGenre)
                .distinct()
                .collect(Collectors.toSet());
    }
}
