package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("UserDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> getAll() {
        return userStorage.getAll();
    }

    public User get(Integer id) throws UserNotFoundException, UserValidationException {
        return userStorage.get(id);
    }

    public void add(User user) {
        userStorage.add(user);
    }

    public void update(User user) throws UserNotFoundException {
        userStorage.update(user);
    }

    public void addFriend(Integer userId, Integer friendId) throws UserValidationException, UserNotFoundException {
        userStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(Integer userId, Integer friendId) throws UserValidationException, UserNotFoundException {
        userStorage.deleteFriend(userId, friendId);
    }

    public Collection<User> getFriends(Integer id) throws UserValidationException, UserNotFoundException {
        Collection<User> result = new TreeSet<>(Comparator.comparing(User::getId));
        for (Integer i : userStorage.getFriends(id)) {
            result.add(userStorage.get(i));
        }
        return result;
    }

    public Collection<User> commonFriends(Integer userId, Integer otherUserId) throws UserValidationException, UserNotFoundException {
        Set<Integer> intersection = new HashSet<>(userStorage.getFriends(userId));
        intersection.retainAll(userStorage.getFriends(otherUserId));
        Collection<User> result = new TreeSet<>(Comparator.comparing(User::getId));
        for (Integer id : intersection) {
            result.add(userStorage.get(id));
        }
        return result;
    }

    public void deleteUserById(Integer userId) {
        userStorage.deleteUserById(userId);
    }

    public Collection<Film> getRecommendations(Integer userId) throws UserValidationException {
        return userStorage.getRecommendations(userId);
    }

    public List<Feed> getFeed(Integer userId) throws UserNotFoundException {
        return userStorage.getFeed(userId);
    }
}
