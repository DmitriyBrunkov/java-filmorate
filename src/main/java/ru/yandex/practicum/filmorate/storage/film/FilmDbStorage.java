package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.FilmValidationException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.queries.FilmQueries;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.TreeSet;

@Component("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Film> getAll() {
        return jdbcTemplate.query(FilmQueries.GET_ALL_FILMS, this::mapRowToFilm);
    }

    @Override
    public Film get(Integer id) throws FilmNotFoundException {
        if (!contains(id)) {
            throw new FilmNotFoundException("ID: " + id + " doesn't exist");
        }
        return jdbcTemplate.queryForObject(FilmQueries.GET_FILM_BY_ID_WITH_MPA_NAMES, this::mapRowToFilm, id);
    }

    @Override
    public void put(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(FilmQueries.ADD_FILM, new String[]{"film_id"});
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            if (!(film.getMpa() == null)) {
                ps.setInt(5, film.getMpa().getId());
            } else {
                ps.setObject(5, null);
            }
            return ps;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        if (!film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(FilmQueries.ADD_GENRE_OF_FILM, film.getId(), genre.getId());
            }
        }
    }

    @Override
    public void update(Film film) throws FilmValidationException, FilmNotFoundException {
        if (film.getId() == null || !contains(film.getId())) {
            throw new FilmNotFoundException("ID: " + film.getId() + " doesn't exist");
        }
        jdbcTemplate.update(FilmQueries.UPDATE_FILM, film.getName(), film.getDescription(),
                Date.valueOf(film.getReleaseDate()), film.getDuration(), film.getMpa().getId(), film.getId());
        jdbcTemplate.update(FilmQueries.DELETE_GENRES_OF_FILM, film.getId());
        if (!film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(FilmQueries.ADD_GENRE_OF_FILM, film.getId(), genre.getId());
            }
        }
    }

    @Override
    public void addLike(Integer filmId, Integer userId) throws FilmValidationException {
        if (filmId == null || !contains(filmId)) {
            throw new FilmValidationException("ID: " + filmId + " doesn't exist");
        }
        jdbcTemplate.update(FilmQueries.ADD_LIKE, filmId, userId);
    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) throws FilmNotFoundException, UserNotFoundException {
        if (filmId == null || !contains(filmId)) {
            throw new FilmNotFoundException("ID: " + filmId + " doesn't exist");
        }
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(FilmQueries.CHECK_LIKE, filmId, userId);
        if (!userRows.next()) {
            throw new UserNotFoundException("User ID: " + userId + " doesn't exist");
        }
        jdbcTemplate.update(FilmQueries.DELETE_LIKE, filmId, userId);
    }

    private boolean contains(Integer id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(FilmQueries.GET_FILM_BY_ID, id);
        return userRows.next();
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(resultSet.getInt("film_id"));
        film.setName(resultSet.getString("name"));
        film.setDescription(resultSet.getString("description"));
        film.setReleaseDate(resultSet.getDate("release_date").toLocalDate());
        film.setDuration(resultSet.getInt("duration"));
        Mpa mpa = new Mpa();
        mpa.setId(resultSet.getInt("films.mpa_id"));
        mpa.setName(resultSet.getString("mpa.name"));
        film.setMpa(mpa);
        film.setLikes(new HashSet<>(jdbcTemplate.query(FilmQueries.GET_FILM_LIKES, (rs, rowNumber) -> rs.getInt("user_id"), film.getId())));
        film.setGenres(new TreeSet<>(jdbcTemplate.query(FilmQueries.GET_FILM_GENRES, this::mapRowToGenre, film.getId())));
        return film;
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        Genre genre = new Genre();
        genre.setId(resultSet.getInt("genres.genre_id"));
        genre.setName(resultSet.getString("name"));
        return genre;
    }
}
