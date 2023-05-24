package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dbStorage.DbStorage;
import ru.yandex.practicum.filmorate.storage.user.queries.UserQueries;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

@Component("UserDbStorage")
public class UserDbStorage extends DbStorage implements UserStorage {
    private enum Status { pending, confirmed }

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public Collection<User> getAll() {
        return jdbcTemplate.query(UserQueries.GET_ALL_USERS, this::mapRowToUser);
    }

    @Override
    public User get(Integer id) throws UserNotFoundException, UserValidationException {
        if (!contains(id)) {
            throw new UserNotFoundException("ID: " + id + " doesn't exist");
        }
        User user = jdbcTemplate.queryForObject(UserQueries.GET_USER_BY_ID, this::mapRowToUser, id);
        assert user != null;
        user.setFriends(new HashSet<>(getFriends(id)));
        return user;
    }

    @Override
    public void add(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(UserQueries.ADD_USER, new String[]{"user_id"});
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setDate(4, Date.valueOf(user.getBirthday()));
            return ps;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
    }

    @Override
    public void addFriend(Integer userId, Integer friendId) throws UserNotFoundException {
        if (userId == null || !contains(userId)) {
            throw new UserNotFoundException("User " + userId + " not found");
        }
        if (friendId == null || !contains(friendId)) {
            throw new UserNotFoundException("User " + friendId + " not found");
        }
        SqlRowSet friendsRows = jdbcTemplate.queryForRowSet(UserQueries.GET_USERS_FRIENDSHIP_STATUS, friendId, userId);
        if (friendsRows.next()) {
            if (friendsRows.getString("status").equals(Status.pending.name())) {
                jdbcTemplate.update(UserQueries.UPDATE_FRIEND_STATUS, Status.confirmed.name(), friendId, userId);
            }
        } else {
            friendsRows = jdbcTemplate.queryForRowSet(UserQueries.GET_USERS_FRIENDSHIP_STATUS, userId, friendId);
            if (!friendsRows.next()) {
                jdbcTemplate.update(UserQueries.ADD_FRIEND, userId, friendId, Status.pending.name());
            }
        }

    }

    @Override
    public void deleteFriend(Integer userId, Integer friendId) throws UserValidationException {
        if (userId == null || !contains(userId)) {
            throw new UserValidationException("User " + userId + " not found");
        }
        if (friendId == null || !contains(friendId)) {
            throw new UserValidationException("User " + friendId + " not found");
        }
        jdbcTemplate.update(UserQueries.DELETE_FRIEND, userId, friendId);
    }

    @Override
    public Collection<Integer> getFriends(Integer id) throws UserNotFoundException {
        if (id == null || !contains(id)) {
            throw new UserNotFoundException("User " + id + " not found");
        }
        return jdbcTemplate.query(UserQueries.GET_USER_FRIENDS, (resultSet, rowNum) -> resultSet.getInt("friend_id"), id, id);
    }

    @Override
    public void update(User user) throws UserNotFoundException {
        if (user.getId() == null || !contains(user.getId())) {
            throw new UserNotFoundException("ID: " + user.getId() + " doesn't exist");
        }
        jdbcTemplate.update(UserQueries.UPDATE_USER, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
    }

    @Override
    public void deleteUserById(Integer userId) {
        jdbcTemplate.update(UserQueries.DELETE_USER_BY_ID, userId);
    }

    @Override
    public Collection<Integer> getRecommendationsId(Integer userId) throws UserValidationException {
        if (userId == null || !contains(userId)) {
            throw new UserValidationException("User " + userId + " not found");
        }
        return jdbcTemplate.query(UserQueries.GET_RECOMMENDATIONS, (resultSet, rowNum) -> resultSet.getInt("film_id"), userId, userId, userId);
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("user_id"));
        user.setEmail(resultSet.getString("email"));
        user.setLogin(resultSet.getString("login"));
        user.setName(resultSet.getString("name"));
        user.setBirthday(resultSet.getDate("birthday").toLocalDate());
        return user;
    }

    private boolean contains(Integer id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(UserQueries.GET_USER_BY_ID, id);
        return userRows.next();
    }
}