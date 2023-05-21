package ru.yandex.practicum.filmorate.storage.genre.queries;

public final class GenreQueries {
    public static final String GET_ALL_GENRES = "select GENRE_ID, NAME from GENRE_NAMES";
    public static final String GET_GENRE = "select GENRE_ID, NAME from GENRE_NAMES where GENRE_ID = ?";
    public static final String GET_ALL_FILM_GENRES = "select film_id, GENRES.GENRE_ID, name from GENRES left join " +
            "GENRE_NAMES GN on GENRES.GENRE_ID = GN.GENRE_ID";
}
