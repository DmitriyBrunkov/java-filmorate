package ru.yandex.practicum.filmorate.storage.user.queries;

public final class UserQueries {
    public static final String GET_ALL_USERS = "select USER_ID, EMAIL, LOGIN, NAME, BIRTHDAY from users";
    public static final String GET_USER_BY_ID = "select USER_ID, EMAIL, LOGIN, NAME, BIRTHDAY from users where user_id = ?";
    public static final String GET_USER_FRIENDS = "select FRIEND_ID from FRIENDS where USER_ID = ? " +
            "union select USER_ID from FRIENDS where FRIEND_ID = ? and STATUS = 'confirmed'";
    public static final String ADD_USER = "insert into users (email, login, name, birthday) values (?, ?, ?, ?)";
    public static final String UPDATE_USER = "update users SET email = ?, login = ?, name = ?, birthday = ? " +
            "WHERE user_id = ?";
    public static final String ADD_FRIEND = "insert into friends (user_id, friend_id, status) values (?, ?, ?)";
    public static final String UPDATE_FRIEND_STATUS = "update friends set status = ? where user_id = ? and friend_id = ?";
    public static final String GET_USERS_FRIENDSHIP_STATUS = "select status from friends where user_id = ? and friend_id = ?";
    public static final String DELETE_FRIEND = "delete from friends where user_id = ? and friend_id = ?";
    public static final String DELETE_USER_BY_ID = "delete from users where user_id = ?";
}
