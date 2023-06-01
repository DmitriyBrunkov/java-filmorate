package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserValidationException;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserStorage {
    Collection<User> getAll();

    User get(Integer id) throws UserNotFoundException, UserValidationException;

    void add(User user);

    void addFriend(Integer userId, Integer friendId) throws UserValidationException, UserNotFoundException;

    void deleteFriend(Integer userId, Integer friendId) throws UserValidationException, UserNotFoundException;

    Collection<Integer> getFriends(Integer id) throws UserNotFoundException;

    void update(User user) throws UserNotFoundException;

    void deleteUserById(Integer userId);

    Collection<Film> getRecommendations(Integer userId) throws UserValidationException;

    List<Feed> getFeed(Integer userId) throws UserNotFoundException;
}