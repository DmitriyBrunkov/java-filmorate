package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotImplException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserValidationException;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("InMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {
    private static int currentId = 1;
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public Collection<User> getAll() {
        return users.values();
    }

    @Override
    public User get(Integer id) throws UserNotFoundException {
        if (!contains(id)) {
            throw new UserNotFoundException("ID: " + id + " doesn't exist");
        }
        return users.get(id);
    }

    @Override
    public void add(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        user.setId(generateId());
        users.put(user.getId(), user);
    }

    @Override
    public void addFriend(Integer userId, Integer friendId) throws UserNotFoundException {
        if (userId == null || !contains(userId)) {
            throw new UserNotFoundException("User " + userId + " not found");
        }
        if (friendId == null || !contains(friendId)) {
            throw new UserNotFoundException("User " + friendId + " not found");
        }
        get(userId).getFriends().add(friendId);
    }

    @Override
    public void deleteFriend(Integer userId, Integer friendId) throws UserValidationException, UserNotFoundException {
        if (userId == null || !contains(userId)) {
            throw new UserValidationException("User " + userId + " not found");
        }
        if (friendId == null || !contains(friendId)) {
            throw new UserValidationException("User " + friendId + " not found");
        }
        get(userId).getFriends().remove(friendId);
    }

    @Override
    public Collection<Integer> getFriends(Integer id) throws UserNotFoundException {
        if (id == null || !contains(id)) {
            throw new UserNotFoundException("User " + id + " not found");
        }
        return users.get(id).getFriends();
    }

    @Override
    public void update(User user) throws UserNotFoundException {
        if (user.getId() == null || !users.containsKey(user.getId())) {
            throw new UserNotFoundException("ID: " + user.getId() + " doesn't exist");
        }
        users.put(user.getId(), user);
    }

    @Override
    public void deleteUserById(Integer userId) {
        users.remove(userId);
    }

    @Override
    public Collection<Film> getRecommendations(Integer userId) {
        throw new NotImplException("InMemoryUserStorage: getRecommendations() not implemented");
    }

    public List<Feed> getFeed(Integer userId) {
        throw new NotImplException("InMemoryUserStorage: getFeed() not implemented");
    }

    //@Override
    private boolean contains(Integer id) {
        return users.containsKey(id);
    }

    private int generateId() {
        while (users.containsKey(currentId)) {
            currentId++;
        }
        return currentId++;
    }
}
