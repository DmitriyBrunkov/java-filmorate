package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.dbStorage.DbStorage;
import ru.yandex.practicum.filmorate.storage.director.queries.DirectorQueries;
import ru.yandex.practicum.filmorate.storage.film.queries.FilmQueries;
import ru.yandex.practicum.filmorate.storage.genre.queries.GenreQueries;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


@Component("FilmDbStorage")
public class FilmDbStorage extends DbStorage implements FilmStorage {

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    private Set<Genre> genres = new TreeSet<>();

    private Set<Director> directors = new HashSet<>();

    @Override
    public Collection<Film> getAll() {
        genres = getGenres();
        directors = getAllDirectors();

        Collection<Film> films = jdbcTemplate.query(FilmQueries.GET_ALL_FILMS, this::mapRowToFilmWoGenres);
        SqlRowSet filmGenreRow = jdbcTemplate.queryForRowSet(GenreQueries.GET_ALL_FILM_GENRES);
        while (filmGenreRow.next()) {
            Genre genre = new Genre();
            genre.setId(filmGenreRow.getInt("genre_id"));
            genre.setName(filmGenreRow.getString("name"));
            films.stream().filter(f -> f.getId().equals(filmGenreRow.getInt("film_id"))).findAny().get()
                    .getGenres().add(genre);
        }
        SqlRowSet filmDirectorRow = jdbcTemplate.queryForRowSet(FilmQueries.GET_ALL_FILM_DIRECTORS);
        while (filmDirectorRow.next()) {
            Director director = new Director();
            director.setId(filmDirectorRow.getInt("director_id"));
            director.setName(filmDirectorRow.getString("name"));
            films.stream().filter(f -> f.getId().equals(filmDirectorRow.getInt("film_id"))).findAny().get()
                    .getDirectors().add(director);
        }
        return films;
    }

    @Override
    public Film get(Integer id) throws FilmNotFoundException {
        if (!contains(id)) {
            throw new FilmNotFoundException("ID: " + id + " doesn't exist");
        }
        genres = getGenres();
        directors = getAllDirectors();
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
        if (!film.getDirectors().isEmpty()) {
            for (Director director : film.getDirectors()) {
                jdbcTemplate.update(FilmQueries.ADD_DIRECTOR_OF_FILM, film.getId(), director.getId());
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
        jdbcTemplate.update(FilmQueries.DELETE_DIRECTORS_OF_FILM, film.getId());
        if (!film.getDirectors().isEmpty()) {
            for (Director director : film.getDirectors()) {
                jdbcTemplate.update(FilmQueries.ADD_DIRECTOR_OF_FILM, film.getId(), director.getId());
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


    @Override
    public List<Film> getFilmsDirectorSorted(Integer directorId, String sortBy) throws DirectorNotFoundException, InvalidParameterException {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(DirectorQueries.GET_DIRECTOR_BY_ID, directorId);
        if (!sqlRowSet.next()) {
            throw new DirectorNotFoundException("ID: " + directorId + " doesn't exist");
        }
        genres = getGenres();
        directors = getAllDirectors();
        switch (sortBy) {
            case "year":
                return jdbcTemplate.query(FilmQueries.GET_FILMS_SORTED_DIRECTOR_BY_YEAR, this::mapRowToFilm, directorId);
            case "likes":
                return jdbcTemplate.query(FilmQueries.GET_FILMS_SORTED_DIRECTOR_BY_LIKES, this::mapRowToFilm, directorId);
        }
        throw new InvalidParameterException("sortBy: " + sortBy + " doesn't exist");
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
        Set<Integer> filmGenresId = new HashSet<>(jdbcTemplate.query(FilmQueries.GET_FILM_GENRES_ID, (rs, rowNumber) -> rs.getInt("genre_id"), film.getId()));
        Set<Genre> filmGenres = new TreeSet<>();
        for (Integer i : filmGenresId) {
            filmGenres.add(genres.stream().filter(g -> g.getId().equals(i)).findAny().get());
        }
        film.setGenres(filmGenres);
        Set<Director> filmDirectors = new HashSet<>();
        Set<Integer> filmDirectorsId = new HashSet<>(jdbcTemplate.query(FilmQueries.GET_FILM_DIRECTORS_ID, (rs, rowNumber) -> rs.getInt("director_id"), film.getId()));
        for (Integer i : filmDirectorsId) {
            filmDirectors.add(directors.stream().filter(g -> g.getId().equals(i)).findAny().get());
        }
        film.setDirectors(filmDirectors);
        return film;
    }

    private Film mapRowToFilmWoGenres(ResultSet resultSet, int rowNum) throws SQLException {
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
        return film;
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        Genre genre = new Genre();
        genre.setId(resultSet.getInt("genre_id"));
        genre.setName(resultSet.getString("name"));
        return genre;
    }

    private Set<Genre> getGenres() {
        return new TreeSet<>(jdbcTemplate.query(GenreQueries.GET_ALL_GENRES, this::mapRowToGenre));
    }


    private Director mapRowToDirector(ResultSet resultSet, int rowNum) throws SQLException {
        Director director = new Director();
        director.setId(resultSet.getInt("director_id"));
        director.setName(resultSet.getString("name"));
        return director;
    }

    private Set<Director> getAllDirectors() {
        return new HashSet<>(jdbcTemplate.query(DirectorQueries.GET_ALL_DIRECTORS, this::mapRowToDirector));
    }
}
