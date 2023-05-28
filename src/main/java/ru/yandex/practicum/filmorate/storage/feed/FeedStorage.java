package ru.yandex.practicum.filmorate.storage.feed;

import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Feed;

import java.util.List;

public interface FeedStorage {
    List<Feed> getFeed(Integer userId);

    void addFeed(int userId, int entityId, String eventType, String operation);
}
