package ru.yandex.practicum.filmorate.storage.reviews;

import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ReviewNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Reviews;

import java.util.List;

public interface ReviewsStorage {
    Reviews postReview (Reviews reviews) throws UserNotFoundException, FilmNotFoundException;

    Reviews updateReview (Reviews reviews) throws ReviewNotFoundException, UserNotFoundException, FilmNotFoundException;

    void deleteReview (Integer id) throws ReviewNotFoundException;

    Reviews getReviewById (Integer id) throws ReviewNotFoundException;

    List<Reviews> getReviewsByFilmId (Integer id, Integer count) throws FilmNotFoundException;

    void addLike(Integer reviewId, Integer userId) throws ReviewNotFoundException, UserNotFoundException;

    void addDislike(Integer reviewId, Integer userId) throws UserNotFoundException, ReviewNotFoundException;

    void deleteLike(Integer reviewId, Integer userId) throws UserNotFoundException, ReviewNotFoundException;

    void deleteDislike(Integer reviewId, Integer userId) throws UserNotFoundException, ReviewNotFoundException;
}