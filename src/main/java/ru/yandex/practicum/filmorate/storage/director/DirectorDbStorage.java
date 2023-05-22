package ru.yandex.practicum.filmorate.storage.director;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DirectorNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.dbStorage.DbStorage;
import ru.yandex.practicum.filmorate.storage.director.queries.DirectorQueries;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Objects;

@Component
public class DirectorDbStorage extends DbStorage implements DirectorStorage {

    public DirectorDbStorage(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public Collection<Director> getAll() {
        return jdbcTemplate.query(DirectorQueries.GET_ALL_DIRECTORS, this::mapRowToDirector);
    }

    @Override
    public Director get(Integer id) throws DirectorNotFoundException {
        if (!contains(id)) {
            throw new DirectorNotFoundException("ID:" + id + " doesn't exist");
        }
        return jdbcTemplate.queryForObject(DirectorQueries.GET_DIRECTOR_BY_ID, this::mapRowToDirector, id);
    }

    @Override
    public void put(Director director) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(DirectorQueries.ADD_DIRECTOR, new String[]{"director_id"});
            ps.setString(1, director.getName());
            return ps;
        }, keyHolder);
        director.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
    }

    @Override
    public void update(Director director) throws DirectorNotFoundException {
        if (director.getId() == null || !contains(director.getId())) {
            throw new DirectorNotFoundException("ID:" + director.getId() + " doesn't exist");
        }
        jdbcTemplate.update(DirectorQueries.UPDATE_DIRECTOR, director.getName(), director.getId());
    }

    @Override
    public void delete(Integer id) throws DirectorNotFoundException {
        if (id == null || !contains(id)) {
            throw new DirectorNotFoundException("ID:" + id + " doesn't exist");
        }
        jdbcTemplate.update(DirectorQueries.DELETE_DIRECTOR, id);
    }

    private boolean contains(Integer id) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(DirectorQueries.GET_DIRECTOR_BY_ID, id);
        return sqlRowSet.next();
    }

    private Director mapRowToDirector(ResultSet resultSet, int rowNum) throws SQLException {
        Director director = new Director();
        director.setId(resultSet.getInt("director_id"));
        director.setName(resultSet.getString("name"));
        return director;
    }
}
