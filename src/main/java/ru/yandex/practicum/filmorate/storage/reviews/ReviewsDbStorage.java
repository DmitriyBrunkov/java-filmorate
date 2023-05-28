package ru.yandex.practicum.filmorate.storage.reviews;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ReviewNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.dbStorage.DbStorage;
import ru.yandex.practicum.filmorate.storage.feed.FeedStorage;
import ru.yandex.practicum.filmorate.storage.film.queries.FilmQueries;
import ru.yandex.practicum.filmorate.storage.reviews.queries.ReviewsQueries;
import ru.yandex.practicum.filmorate.storage.user.queries.UserQueries;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Component("ReviewsDbStorage")
public class ReviewsDbStorage extends DbStorage implements ReviewsStorage {
    private final FeedStorage feedStorage;

    @Autowired
    public ReviewsDbStorage(JdbcTemplate jdbcTemplate, FeedStorage feedStorage) {
        super(jdbcTemplate);
        this.feedStorage = feedStorage;
    }

    @Override
    public Review postReview(Review review) throws UserNotFoundException, FilmNotFoundException {
        checkUserAndFilm(review);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(ReviewsQueries.ADD_REVIEW, new String[]{"review_id"});
            ps.setString(1, review.getContent());
            ps.setBoolean(2, review.getIsPositive());
            ps.setInt(3, review.getUserId());
            ps.setInt(4, review.getFilmId());
            if (review.getUseful() == null) {
                ps.setInt(5, 0);
            } else {
                ps.setInt(5, review.getUseful());
            }
            return ps;
        }, keyHolder);
        review.setReviewId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        feedStorage.addFeed(review.getUserId(), review.getReviewId(), "REVIEW", "ADD");
        return review;
    }

    @Override
    public Review updateReview(Review review)
            throws ReviewNotFoundException, UserNotFoundException, FilmNotFoundException {
        checkUserAndFilm(review);
        Integer reviewId = review.getReviewId();
        jdbcTemplate.update(ReviewsQueries.UPDATE_REVIEW,
                review.getContent(), review.getIsPositive(), reviewId);
        feedStorage.addFeed(review.getUserId(), review.getReviewId(), "REVIEW", "UPDATE");
        return getReviewById(reviewId);
    }

    @Override
    public void deleteReview(Integer id) throws ReviewNotFoundException {
        if (!contains(id)) {
            throw new ReviewNotFoundException("Отзыв с id = " + id + " не найден.");
        }
        Review review = getReviewById(id);
        feedStorage.addFeed(review.getUserId(), review.getReviewId(), "REVIEW", "REMOVE");
        jdbcTemplate.update(ReviewsQueries.DELETE_REVIEW, id);
    }

    @Override
    public Review getReviewById(Integer id) throws ReviewNotFoundException {
        if (!contains(id)) {
            throw new ReviewNotFoundException("Отзыв с id = " + id + " не найден.");
        }

        return jdbcTemplate.queryForObject(ReviewsQueries.GET_REVIEW_BY_ID, this::mapRowToReview, id);
    }

    @Override
    public List<Review> getReviewsByFilmId(Integer id, Integer count) throws FilmNotFoundException {
        if (id == null) {
            return jdbcTemplate.query(ReviewsQueries.GET_REVIEW_WITHOUT_FILM_ID, this::mapRowToReview, count);
        } else {
            SqlRowSet filmRows = jdbcTemplate.queryForRowSet(FilmQueries.GET_FILM_BY_ID, id);
            if (filmRows.next()) {
                return jdbcTemplate.query(ReviewsQueries.GET_REVIEW_BY_FILM_ID, this::mapRowToReview, id, count);
            } else {
                throw new FilmNotFoundException("Фильм с id = " + id + " не найден.");
            }
        }
    }

    @Override
    public void addLike(Integer reviewId, Integer userId) throws ReviewNotFoundException, UserNotFoundException {
        checkReviewAndUser(reviewId, userId);
        jdbcTemplate.update(ReviewsQueries.ADD_LIKE_OR_DISLIKE, reviewId, userId, Usefulness.USEFUL.getValue());
    }

    @Override
    public void addDislike(Integer reviewId, Integer userId) throws UserNotFoundException, ReviewNotFoundException {
        checkReviewAndUser(reviewId, userId);
        jdbcTemplate.update(ReviewsQueries.ADD_LIKE_OR_DISLIKE, reviewId, userId, Usefulness.USELESS.getValue());
    }

    @Override
    public void deleteLike(Integer reviewId, Integer userId) throws UserNotFoundException, ReviewNotFoundException {
        checkReviewAndUsersLike(reviewId, userId);
        jdbcTemplate.update(ReviewsQueries.DELETE_LIKE_OR_DISLIKE, reviewId, userId, Usefulness.USELESS.getValue());
    }

    @Override
    public void deleteDislike(Integer reviewId, Integer userId) throws UserNotFoundException, ReviewNotFoundException {
        checkReviewAndUsersLike(reviewId, userId);
        jdbcTemplate.update(ReviewsQueries.DELETE_LIKE_OR_DISLIKE, reviewId, userId, Usefulness.USEFUL.getValue());
    }

    private Review mapRowToReview(ResultSet resultSet, int rowNum) throws SQLException {
        Review review = new Review();
        review.setReviewId(resultSet.getInt("review_id"));
        review.setContent(resultSet.getString("content"));
        review.setIsPositive(resultSet.getBoolean("is_positive"));
        review.setUserId(resultSet.getInt("user_id"));
        review.setFilmId(resultSet.getInt("film_id"));
        review.setUseful(resultSet.getInt("useful"));
        return review;
    }

    private boolean contains(Integer id) {
        SqlRowSet reviewRows = jdbcTemplate.queryForRowSet(ReviewsQueries.GET_REVIEW_BY_ID, id);
        return reviewRows.next();
    }


    private void checkReviewAndUser(Integer reviewId, Integer userId)
            throws ReviewNotFoundException, UserNotFoundException {
        if (reviewId == null || !contains(reviewId)) {
            throw new ReviewNotFoundException("Отзыв с id = " + reviewId + " не найден.");
        }
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(UserQueries.GET_USER_BY_ID, userId);
        if (!userRows.next()) {
            throw new UserNotFoundException("Пользователь с id = " + userId + " не найден.");
        }
    }

    private void checkReviewAndUsersLike(Integer reviewId, Integer userId)
            throws ReviewNotFoundException, UserNotFoundException {
        if (reviewId == null || !contains(reviewId)) {
            throw new ReviewNotFoundException("Отзыв с id = " + reviewId + " не найден.");
        }
        SqlRowSet userLikeRows = jdbcTemplate.queryForRowSet(ReviewsQueries.CHECK_REVIEWS_LIKE, reviewId, userId);
        if (!userLikeRows.next()) {
            throw new UserNotFoundException("Пользователь с id = " + userId + " еще не ставил лайк/дизлайк.");
        }
    }

    private void checkUserAndFilm(Review review) throws UserNotFoundException, FilmNotFoundException {
        Integer userId = review.getUserId();
        Integer filmId = review.getFilmId();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(UserQueries.GET_USER_BY_ID, userId);
        if (!userRows.next()) {
            throw new UserNotFoundException("Пользователь с id = " + userId + " не найден.");
        }
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(FilmQueries.GET_FILM_BY_ID, filmId);
        if (!filmRows.next()) {
            throw new FilmNotFoundException("Фильм с id = " + userId + " не найден.");
        }
    }
}