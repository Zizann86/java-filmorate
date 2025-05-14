package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Repository
public class RatingRepository extends BaseRepository<Rating> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM ratings";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM ratings WHERE id = ?";

    public RatingRepository(JdbcTemplate jdbc, RowMapper<Rating> mapper) {
        super(jdbc, mapper);
    }

    public Optional<Rating> getRatingById(long ratingId) {
        return findOne(FIND_BY_ID_QUERY, ratingId);
    }

    public Collection<Rating> getRatingAll() {
        return findMany(FIND_ALL_QUERY);
    }
}
