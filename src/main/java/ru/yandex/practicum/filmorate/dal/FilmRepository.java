package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;

@Slf4j
@Repository
public class FilmRepository extends BaseRepository<Film> implements FilmStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM films";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM films WHERE film_id = ?";
    private static final String FIND_INSERT_FILM =
            "INSERT INTO films (name, description, releaseDate, duration, rating) " +
                    "VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE films SET name = ?, description = ?, releaseDate = ?, duration = ?, rating = ?" +
            "WHERE film_id = ?";
    private static final String DELETE_GENRES_BY_FILM_ID = "DELETE FROM film_genres WHERE film_id = ?";
    private static final String INSERT_GENRE = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
    private static final String ADD_LIKE = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";
    private static final String DELETE_LIKE = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
    private static final String CHEK_MPA = "SELECT COUNT(*) FROM ratings WHERE id = ?";
    private static final String CHEK_GENRE = "SELECT COUNT(*) FROM genres WHERE genre_id = ?";
    private static final String GET_GENRE_BY_ID = "SELECT g.* FROM genres g " +
            "JOIN film_genres fg ON g.genre_id = fg.genre_id " +
            "WHERE fg.film_id = ?";
    private static final String GET_TOP_FILMS = "SELECT f.* FROM films f LEFT JOIN likes l ON f.film_id = l.film_id" +
            " GROUP BY f.film_id ORDER BY COUNT(l.user_id) DESC LIMIT ?";

    public FilmRepository(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Optional<Film> getFilmById(Long filmId) {
        return findOne(FIND_BY_ID_QUERY, filmId);
    }

    @Override
    public List<Film> getAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Film create(Film film) {
        long id = insert(
                FIND_INSERT_FILM,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId()
        );
        film.setId(id);
        insertGenres(id, film.getGenres());
        return film;
    }

    @Override
    public Film update(Film film) {
        update(UPDATE_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );
        update(DELETE_GENRES_BY_FILM_ID, film.getId());
        insertGenres(film.getId(), film.getGenres());
        log.info("Фильм обновлён в БД: {}", film);
        return film;
    }

    public void addLike(long filmId, long userId) {
        jdbc.update(ADD_LIKE, filmId, userId);
    }

    public boolean deleteLike(long filmId, long userId) {
        int rowsDeleted  = this.jdbc.update(DELETE_LIKE, filmId, userId);
        return rowsDeleted > 0;
    }

    public boolean existsByRatingId(long ratingId) {
        Integer count = jdbc.queryForObject(CHEK_MPA, new Object[]{ratingId}, Integer.class);
        return count != null && count > 0;
    }

    public boolean genreExists(long genreId) {
        Integer count = jdbc.queryForObject(CHEK_GENRE, new Object[]{genreId}, Integer.class);
        return count != null && count > 0;
    }

    public Collection<Film> getTopFilms(int count) {
         return jdbc.query(GET_TOP_FILMS, mapper, count);
    }

    public Set<Genre> getGenresByFilmId(Long filmId) {
        return new HashSet<>(jdbc.query(GET_GENRE_BY_ID, new GenreRowMapper(), filmId));
    }

    private void insertGenres(long filmId, Set<Genre> genres) {
        if (genres == null || genres.isEmpty()) return;
        Set<Long> uniqueGenreIds = new HashSet<>();
        for (Genre genre : genres) {
            if (genre.getId() != null) {
                uniqueGenreIds.add(genre.getId());
            }
        }
        for (Long genreId : uniqueGenreIds) {
            jdbc.update(INSERT_GENRE, filmId, genreId);
        }
    }
}