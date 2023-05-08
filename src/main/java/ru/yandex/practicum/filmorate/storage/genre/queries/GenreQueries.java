package ru.yandex.practicum.filmorate.storage.genre.queries;

public final class GenreQueries {
    public static final String GET_ALL_GENRES = "select * from GENRE_NAMES";
    public static final String GET_GENRE = "select * from GENRE_NAMES where GENRE_ID = ?";
}
