package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.GenreRepository;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    public Collection<GenreDto> findAllGenres() {
        return genreRepository.getGenreAll().stream()
                .map(genreMapper::mapToGenreDto)
                .collect(Collectors.toList());
    }

    public Optional<GenreDto> findGenreById(long genreId) {
        return genreRepository.getGenreById(genreId)
                .map(genreMapper::mapToGenreDto);
    }
}
