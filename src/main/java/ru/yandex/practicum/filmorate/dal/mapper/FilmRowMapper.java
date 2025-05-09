package ru.yandex.practicum.filmorate.dal.mapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.RatingRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
@Component
@AllArgsConstructor
public class FilmRowMapper implements RowMapper<Film> {
    RatingRepository ratingRepository;

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong("film_id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("releaseDate").toLocalDate());
        film.setDuration(rs.getLong("duration"));
        Long mpaId = rs.getLong("rating");
        Rating rating = ratingRepository.getRatingById(mpaId)
                .orElseThrow(() -> new NotFoundException("MPA рейтинг не найден по id: " + mpaId));
        film.setMpa(rating);
        log.info("Сопоставленный фильм: {}", film);
        return film;
    }
}
