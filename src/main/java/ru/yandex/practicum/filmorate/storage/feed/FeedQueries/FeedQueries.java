package ru.yandex.practicum.filmorate.storage.feed.FeedQueries;

public final class FeedQueries {

    private FeedQueries() {
    }

    public static final String GET_FEED = "select EVENT_ID, USER_ID, ENTITY_ID, TIME_STAMP, EVENT_TYPE, OPERATION " +
            "from feed;";
    public static final String ADD_FEED = "insert into FEED (USER_ID, ENTITY_ID, EVENT_TYPE, OPERATION) " +
            "VALUES (?, ?, ?, ?);";
}
