package ru.yandex.practicum.filmorate.storage.mpa.queries;

public final class MpaQueries {
    public static final String GET_ALL_MPA = "select MPA_ID, NAME from MPA";
    public static final String GET_MPA = "select MPA_ID, NAME from MPA where MPA_ID = ?";
}
