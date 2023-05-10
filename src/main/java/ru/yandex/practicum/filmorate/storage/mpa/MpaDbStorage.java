package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.dbStorage.DbStorage;
import ru.yandex.practicum.filmorate.storage.mpa.queries.MpaQueries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Component
public class MpaDbStorage extends DbStorage implements MpaStorage {

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public Collection<Mpa> getAll() {
        return jdbcTemplate.query(MpaQueries.GET_ALL_MPA, this::mapRowToMpa);
    }

    @Override
    public Mpa get(Integer id) throws MpaNotFoundException {
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet(MpaQueries.GET_MPA, id);
        if (!mpaRows.next()) {
            throw new MpaNotFoundException("Mpa with id: " + id + " not found");
        }
        Mpa mpa = new Mpa();
        mpa.setId(mpaRows.getInt("mpa_id"));
        mpa.setName(mpaRows.getString("name"));
        return mpa;
    }

    private Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        Mpa mpa = new Mpa();
        mpa.setId(resultSet.getInt("mpa_id"));
        mpa.setName(resultSet.getString("name"));
        return mpa;
    }
}
