package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ReviewNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Reviews;
import ru.yandex.practicum.filmorate.storage.reviews.ReviewsStorage;

import java.util.List;

@Service
public class ReviewsService {

    private final ReviewsStorage reviewsStorage;

    @Autowired
    public ReviewsService(@Qualifier("ReviewsDbStorage") ReviewsStorage reviewsStorage) {
        this.reviewsStorage = reviewsStorage;
    }

    public Reviews postReview (Reviews reviews) throws UserNotFoundException, FilmNotFoundException {
        return reviewsStorage.postReview(reviews);
    };

    public Reviews updateReview (Reviews reviews)
            throws ReviewNotFoundException, UserNotFoundException, FilmNotFoundException {
        return reviewsStorage.updateReview(reviews);
    };

    public void deleteReview (Integer id) throws ReviewNotFoundException {
        reviewsStorage.deleteReview(id);
    };

    public Reviews getReviewById (Integer id) throws ReviewNotFoundException {
        return reviewsStorage.getReviewById(id);
    }

    public List<Reviews> getReviewsByFilmId (Integer id, Integer count) throws FilmNotFoundException {
        return reviewsStorage.getReviewsByFilmId(id, count);
    };

    public void addLike(Integer reviewId, Integer userId) throws ReviewNotFoundException, UserNotFoundException{
        reviewsStorage.addLike(reviewId, userId);
    };

    public void addDislike(Integer reviewId, Integer userId) throws UserNotFoundException, ReviewNotFoundException {
        reviewsStorage.addDislike(reviewId, userId);
    };

    public void deleteLike(Integer reviewId, Integer userId) throws UserNotFoundException, ReviewNotFoundException {
        reviewsStorage.deleteLike(reviewId, userId);
    };

    public void deleteDislike(Integer reviewId, Integer userId) throws UserNotFoundException, ReviewNotFoundException {
        reviewsStorage.deleteDislike(reviewId, userId);
    };

}