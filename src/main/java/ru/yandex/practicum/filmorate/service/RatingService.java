package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.RatingRepository;
import ru.yandex.practicum.filmorate.dto.RatingDto;
import ru.yandex.practicum.filmorate.mapper.RatingMapper;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RatingService {
    private final RatingRepository ratingRepository;
    private final RatingMapper ratingMapper;

    public Optional<RatingDto> findRatingById(long ratingId) {
        return ratingRepository.getRatingById(ratingId)
                .map(ratingMapper::mapToRatingDto);
    }

    public Collection<RatingDto> findAllRatings() {
        return ratingRepository.getRatingAll().stream()
                .map(ratingMapper::mapToRatingDto)
                .collect(Collectors.toList());
    }
}
