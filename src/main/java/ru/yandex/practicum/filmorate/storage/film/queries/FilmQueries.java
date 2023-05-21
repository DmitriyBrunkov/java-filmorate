package ru.yandex.practicum.filmorate.storage.film.queries;

public final class FilmQueries {
    public static final String GET_ALL_FILMS = "select FILMS.film_id, FILMS.name, FILMS.description, FILMS.release_date," +
            " FILMS.duration, FILMS.mpa_id, MPA.mpa_id, MPA.name from films left outer join mpa on films.mpa_id = mpa.mpa_id";
    public static final String ADD_FILM = "insert into films (name, description, release_date, duration, mpa_id) " +
            "values (?, ?, ?, ?, ?)";
    public static final String GET_FILM_BY_ID = "select FILM_ID, FILMS.NAME, DESCRIPTION, RELEASE_DATE, DURATION, " +
            "MPA_ID from films where film_id = ?";
    public static final String GET_FILM_BY_ID_WITH_MPA_NAMES = "select FILMS.film_id, FILMS.name, FILMS.description, " +
            "FILMS.release_date, FILMS.duration, FILMS.mpa_id, MPA.mpa_id, MPA.name from films left outer join mpa on " +
            "films.mpa_id = mpa.mpa_id where film_id = ?";
    public static final String GET_FILM_GENRES_ID = "select GENRE_ID from GENRES WHERE FILM_ID=?";
    public static final String ADD_GENRE_OF_FILM = "insert into GENRES (FILM_ID, GENRE_ID) VALUES (?, ?)";
    public static final String UPDATE_FILM = "update FILMS set NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, " +
            "DURATION = ?, MPA_ID = ? where FILM_ID = ?";
    public static final String DELETE_GENRES_OF_FILM = "delete from GENRES where FILM_ID = ?";
    public static final String ADD_LIKE = "insert into LIKES (FILM_ID, USER_ID) VALUES ( ?, ?)";
    public static final String DELETE_LIKE = "delete from LIKES where FILM_ID = ? and  USER_ID = ?";
    public static final String CHECK_LIKE = "select film_id, user_id from LIKES where FILM_ID = ? and USER_ID = ?";
    public static final String GET_FILM_LIKES = "select USER_ID from LIKES where FILM_ID = ?";
}
