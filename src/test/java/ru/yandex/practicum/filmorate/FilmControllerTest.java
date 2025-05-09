package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/*@SpringBootTest
public class FilmControllerTest {

    @Autowired
    private Validator validator;
    Film film;

    @Autowired
    private FilmController filmController;

    @BeforeEach
    void beforeEach() {
        film = new Film();
        film.setName("Film");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1895, 12, 29));
        film.setDuration(145L);
    }

    @Test
    void testValidFilm() {
        Film createdFilm  = filmController.create(film);
        List<Film> films = filmController.getAll();
        assertEquals(1, films.size(), "Сервер не вернул фильм");
        assertEquals(createdFilm, films.get(0), "Созданный фильм не совпадает с полученным");
    }

    @Test
    void testNullNameForFilm() {
        film.setName(null);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size());
        assertEquals("Название фильма не должно быть null или пустым", violations.iterator().next().getMessage());
    }

    @Test
    void testSizeDescription() {
        film.setDescription("qqqqqqqqqq".repeat(21));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size());
        assertEquals("Описание фильма не должно превышать 200 символов", violations.iterator().next().getMessage());
    }

    @Test
    void testReleaseDate() {
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size());
        assertEquals("Дата релиза — не раньше 28 декабря 1895 года", violations.iterator().next().getMessage());
    }

    @Test
    void testDurationNegative() {
        film.setDuration(-1L);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size());
        assertEquals("Продолжительность фильма должна быть положительным числом", violations.iterator().next().getMessage());
    }
}*/
