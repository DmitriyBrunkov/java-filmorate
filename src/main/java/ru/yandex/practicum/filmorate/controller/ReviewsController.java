package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ReviewNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewsService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/reviews")
public class ReviewsController {

    private final ReviewsService reviewsService;

    @Autowired
    public ReviewsController(ReviewsService reviewsService) {
        this.reviewsService = reviewsService;
    }

    @PostMapping
    public Review postReview(@Valid @RequestBody Review review) throws UserNotFoundException, FilmNotFoundException {
        log.info("Добавлен отзыв с id = {}", review.getReviewId());
        return reviewsService.postReview(review);
    }

    @PutMapping
    public Review updateReview(@Valid @RequestBody Review review)
            throws ReviewNotFoundException, UserNotFoundException, FilmNotFoundException {
        log.info("Отзыв с id = {} обновлен", review.getReviewId());
        return reviewsService.updateReview(review);
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable Integer id) throws ReviewNotFoundException {
        log.info("Отзыв с id = {} удален.", id);
        reviewsService.deleteReview(id);
    }

    @GetMapping("/{id}")
    public Review getReviewById(@PathVariable Integer id) throws ReviewNotFoundException {
        log.info("Отзыв с id = {} получен", id);
        return reviewsService.getReviewById(id);
    }

    @GetMapping
    public List<Review> getReviewsByFilmId(
            @RequestParam(value = "filmId", required = false) Integer filmId,
            @RequestParam(value = "count", defaultValue = "10") Integer count) throws FilmNotFoundException {
        if (filmId != null) {
            log.info("Получены самые полезные отзывы в количестве {} для фильма с id = {} ", count, filmId);
        } else {
            log.info("Самые полезные отзывы в количестве {} получены", count);
        }
        return reviewsService.getReviewsByFilmId(filmId, count);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(
            @PathVariable(value = "id") Integer reviewId,
            @PathVariable(value = "userId") Integer userId) throws ReviewNotFoundException, UserNotFoundException {
        log.info("Поставлен лайк к ревью с id = {}.", reviewId);
        reviewsService.addLike(reviewId, userId);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public void addDislike(
            @PathVariable(value = "id") Integer reviewId,
            @PathVariable(value = "userId") Integer userId) throws UserNotFoundException, ReviewNotFoundException {
        log.info("Поставлен дизлайк к ревью с id = {}.", reviewId);
        reviewsService.addDislike(reviewId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(
            @PathVariable(value = "id") Integer reviewId,
            @PathVariable(value = "userId") Integer userId) throws UserNotFoundException, ReviewNotFoundException {
        log.info("Удален лайк к ревью с id = {}.", reviewId);
        reviewsService.deleteLike(reviewId, userId);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public void deleteDislike(@PathVariable(value = "id") Integer reviewId,
                              @PathVariable(value = "userId") Integer userId) throws UserNotFoundException, ReviewNotFoundException {
        log.info("Удален дизлайк к ревью с id = {}.", reviewId);
        reviewsService.deleteDislike(reviewId, userId);
    }
}
