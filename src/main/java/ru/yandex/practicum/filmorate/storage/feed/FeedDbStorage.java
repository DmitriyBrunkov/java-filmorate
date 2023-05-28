package ru.yandex.practicum.filmorate.storage.feed;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.storage.dbStorage.DbStorage;
import ru.yandex.practicum.filmorate.storage.feed.FeedQuries.FeedQueries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Component("FeedDbStorage")
public class FeedDbStorage extends DbStorage implements FeedStorage {

    public FeedDbStorage(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public List<Feed> getFeed(Integer userId) {
        return jdbcTemplate.query(FeedQueries.GET_FEED, this::mapRowToFeed).stream()
                .filter(userId != null ? feed -> feed.getUserId() == userId : feed -> true)
                .collect(Collectors.toList());
    }

    @Override
    public void addFeed(int userId, int entityId, String eventType, String operation) {
        jdbcTemplate.update(FeedQueries.ADD_FEED, userId, entityId, eventType, operation);
    }

    private Feed mapRowToFeed(ResultSet resultSet, int rowNum) throws SQLException {
        Feed feed = new Feed();
        feed.setEventId(resultSet.getInt("event_id"));
        feed.setUserId(resultSet.getInt("user_id"));
        feed.setEntityId(resultSet.getInt("entity_id"));
        feed.setTimestamp(resultSet.getTimestamp("time_stamp").getTime());
        feed.setEventType(resultSet.getString("event_type"));
        feed.setOperation(resultSet.getString("operation"));
        return feed;
    }
}
