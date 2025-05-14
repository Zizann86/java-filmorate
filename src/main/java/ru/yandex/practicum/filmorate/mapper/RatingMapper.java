package ru.yandex.practicum.filmorate.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.RatingDto;
import ru.yandex.practicum.filmorate.model.Rating;


@Component
public class RatingMapper {

    public RatingDto mapToRatingDto(Rating mpa) {
        return new RatingDto(mpa.getId(), mpa.getName());
    }

    public Rating mapToRating(RatingDto ratingDto) {
        if (ratingDto == null) {
            return null;
        }
        return new Rating(ratingDto.getId(), ratingDto.getName());
    }
}
