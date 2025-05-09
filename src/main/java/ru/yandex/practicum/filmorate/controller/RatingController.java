package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.RatingDto;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.service.RatingService;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class RatingController {
    private final RatingService ratingService;

    @GetMapping
    public Collection<RatingDto> getAllMpa() {
        return ratingService.findAllRatings();
    }

    @GetMapping("/{id}")
    public RatingDto getMpaById(@PathVariable long id) {
        return ratingService.findRatingById(id)
                .orElseThrow(() -> new UserNotFoundException("Rating not found with id " + id));
    }
}
