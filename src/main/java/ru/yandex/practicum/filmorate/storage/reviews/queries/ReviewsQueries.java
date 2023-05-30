package ru.yandex.practicum.filmorate.storage.reviews.queries;

public final class ReviewsQueries {

    private ReviewsQueries() {
    }

    public static final String ADD_REVIEW =
            "INSERT INTO reviews (content, is_positive, user_id, film_id, useful) VALUES (?, ?, ?, ?, ?)";
    public static final String UPDATE_REVIEW =
            "UPDATE reviews SET content = ?, is_positive = ? WHERE review_id = ?";
    public static final String DELETE_REVIEW = "DELETE FROM reviews WHERE review_id = ?";
    public static final String GET_REVIEW_BY_ID =
            "SELECT r.review_id, " +
                    "r.content, " +
                    "r.is_positive, " +
                    "r.user_id, " +
                    "r.film_id, " +
                    "SUM (rl.usefulness) AS useful " +
                    "FROM reviews AS r " +
                    "LEFT JOIN review_likes AS rl " +
                    "ON r.review_id = rl.review_id " +
                    "WHERE r.review_id = ? " +
                    "GROUP BY r.review_id " +
                    "ORDER BY useful DESC";

    public static final String GET_REVIEW_BY_FILM_ID =
            "SELECT r.review_id, " +
                    "r.content, " +
                    "r.is_positive, " +
                    "r.user_id, " +
                    "r.film_id, " +
                    "COALESCE (SUM (rl.usefulness), 0) AS useful " +
                    "FROM reviews AS r " +
                    "LEFT JOIN review_likes AS rl " +
                    "ON r.review_id = rl.review_id " +
                    "WHERE r.film_id = ? " +
                    "GROUP BY r.review_id " +
                    "ORDER BY useful DESC " +
                    "LIMIT ?";

    public static final String GET_REVIEW_WITHOUT_FILM_ID =
            "SELECT r.review_id, " +
                    "r.content, " +
                    "r.is_positive, " +
                    "r.user_id, " +
                    "r.film_id, " +
                    "COALESCE (SUM (rl.usefulness), 0) AS useful " +
                    "FROM reviews AS r " +
                    "LEFT JOIN review_likes AS rl " +
                    "ON r.review_id = rl.review_id " +
                    "GROUP BY r.review_id " +
                    "ORDER BY useful DESC " +
                    "LIMIT ?";

    public static final String ADD_LIKE_OR_DISLIKE =
            "INSERT INTO review_likes (review_id, user_id, usefulness) VALUES (? ,?, ?)";


    public static final String DELETE_LIKE_OR_DISLIKE =
            "DELETE FROM review_likes " +
                    "WHERE review_id = ? " +
                    "AND user_id = ? " +
                    "AND usefulness = ?";

    public static final String CHECK_REVIEWS_LIKE =
            "SELECT review_id, user_id " +
                    "FROM review_likes " +
                    "WHERE review_id = ? AND user_id = ?";
}