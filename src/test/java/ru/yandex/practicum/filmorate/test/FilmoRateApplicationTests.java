package ru.yandex.practicum.filmorate.test;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dal.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.time.ZoneId;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@JdbcTest
@AutoConfigureTestDatabase
@Import({UserRepository.class, UserRowMapper.class})
class FilmoRateApplicationTests {

    @Autowired
    private UserRepository userRepository;
    private User testUser;
    private Faker faker;

    @BeforeEach
    public void setUp() {
        faker = new Faker();
        testUser = new User(
                null,
                faker.internet().emailAddress(),
                faker.name().username().replaceAll("\\s+", ""),
                faker.name().fullName(),
                faker.date().birthday(20, 30).toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate(),
                new HashSet<>(),
                new HashSet<>()
        );
        userRepository.create(testUser);

        User testUser2 = new User(
                null,
                faker.internet().emailAddress(),
                faker.name().username().replaceAll("\\s+", ""),
                faker.name().fullName(),
                faker.date().birthday(20, 30).toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate(),
                new HashSet<>(),
                new HashSet<>()
        );
        userRepository.create(testUser2);
    }

    @Test
    public void testFindUserById() {
        Optional<User> userOptional = userRepository.getById(testUser.getId());

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", testUser.getId())
                );
    }

    @Test
    public void testFindUserAll() {
        List<User> users = userRepository.getAll();

        assertThat(users)
                .hasSize(2);
    }

    @Test
    public void testUpdateUser() {
        testUser.setName("Tom");

        userRepository.update(testUser);
        User updatedUser = userRepository.getById(testUser.getId()).orElseThrow(() -> new RuntimeException("Ошибка при обновлении User"));

        assertThat(updatedUser)
                .hasFieldOrPropertyWithValue("name", "Tom");
    }
}
