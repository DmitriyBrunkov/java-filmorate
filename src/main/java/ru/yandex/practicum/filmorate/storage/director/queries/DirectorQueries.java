package ru.yandex.practicum.filmorate.storage.director.queries;

public class DirectorQueries {
    public static final String GET_ALL_DIRECTORS = "select director_id, name from directors";
    public static final String GET_DIRECTOR_BY_ID = "select director_id, name from directors where director_id = ?";
    public static final String ADD_DIRECTOR = "insert into directors(name) values (?)";
    public static final String UPDATE_DIRECTOR = "update directors set name = ? where director_id = ?";
    public static final String DELETE_DIRECTOR = "delete from directors where director_id = ?";

}
