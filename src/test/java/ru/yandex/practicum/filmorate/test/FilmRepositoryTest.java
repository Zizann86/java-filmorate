package ru.yandex.practicum.filmorate.test;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dal.RatingRepository;
import ru.yandex.practicum.filmorate.dal.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.ZoneId;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@SpringBootTest
@AutoConfigureTestDatabase
@Import({FilmRepository.class, FilmRowMapper.class, RatingRepository.class})
public class FilmRepositoryTest {
    @Autowired
    private FilmRepository filmRepository;
    private Film testFilm;
    private Faker faker;

    @BeforeEach
    public void setUp() {

        faker = new Faker();
        Rating mpaRating = new Rating();
        mpaRating.setId(1L);
        mpaRating.setName("PG-13");
        Set<Genre> genres = new HashSet<>();
        genres.add(new Genre(1L, "Комедия"));
        genres.add(new Genre(2L, "Драма"));
        genres.add(new Genre(3L, "Экшн"));
        testFilm = new Film(
                null,
                faker.name().username().replaceAll("\\s+", ""),
                faker.name().fullName(),
                faker.date().birthday(20, 30).toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate(),
                Long.valueOf(faker.number().numberBetween(10, 60)),
                new HashSet<>(),
                mpaRating,
                genres
        );
        filmRepository.create(testFilm);

        Film testFilm2 = new Film(
                null,
                faker.name().username().replaceAll("\\s+", ""),
                faker.name().fullName(),
                faker.date().birthday(20, 30).toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate(),
                Long.valueOf(faker.number().numberBetween(10, 60)),
                new HashSet<>(),
                mpaRating,
                genres
        );
        filmRepository.create(testFilm2);
    }

    @Test
    public void testFindFilmById() {
        Optional<Film> filmOptional = filmRepository.getFilmById(testFilm.getId());

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", testFilm.getId())
                );
    }

    @Test
    public void testFindFilmAll() {
        List<Film> films = filmRepository.getAll();

        assertThat(films)
                .isNotEmpty();
    }

    @Test
    public void testUpdateFilm() {
        testFilm.setName("Tanks");

        filmRepository.update(testFilm);

        Film updatedFilm = filmRepository.getFilmById(testFilm.getId()).orElseThrow(() -> new RuntimeException("Ошибка при обновлении фильма"));

        assertThat(updatedFilm)
                .hasFieldOrPropertyWithValue("name", "Tanks");
    }


}
