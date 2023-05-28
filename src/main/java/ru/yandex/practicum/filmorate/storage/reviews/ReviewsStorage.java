package ru.yandex.practicum.filmorate.storage.reviews;

import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ReviewNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewsStorage {
    Review postReview(Review review) throws UserNotFoundException, FilmNotFoundException;

    Review updateReview(Review review) throws ReviewNotFoundException, UserNotFoundException, FilmNotFoundException;

    void deleteReview(Integer id) throws ReviewNotFoundException;

    Review getReviewById(Integer id) throws ReviewNotFoundException;

    List<Review> getReviewsByFilmId(Integer id, Integer count) throws FilmNotFoundException;

    void addLike(Integer reviewId, Integer userId) throws ReviewNotFoundException, UserNotFoundException;

    void addDislike(Integer reviewId, Integer userId) throws UserNotFoundException, ReviewNotFoundException;

    void deleteLike(Integer reviewId, Integer userId) throws UserNotFoundException, ReviewNotFoundException;

    void deleteDislike(Integer reviewId, Integer userId) throws UserNotFoundException, ReviewNotFoundException;
}