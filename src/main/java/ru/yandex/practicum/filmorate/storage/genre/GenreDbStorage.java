package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.queries.GenreQueries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Component
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Genre> getAll() {
        return jdbcTemplate.query(GenreQueries.GET_ALL_GENRES, this::mapRowToGenre);
    }

    @Override
    public Genre get(Integer id) throws GenreNotFoundException {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(GenreQueries.GET_GENRE, id);
        if (!genreRows.next()) {
            throw new GenreNotFoundException("Genre with id: " + id + " not found");
        }
        Genre genre = new Genre();
        genre.setId(genreRows.getInt("genre_id"));
        genre.setName(genreRows.getString("name"));
        return genre;
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        Genre genre = new Genre();
        genre.setId(resultSet.getInt("genre_id"));
        genre.setName(resultSet.getString("name"));
        return genre;
    }
}
