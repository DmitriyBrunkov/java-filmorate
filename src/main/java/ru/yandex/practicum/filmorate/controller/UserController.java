package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public User get(@PathVariable("id") Integer id) throws UserNotFoundException, UserValidationException {
        return userService.get(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") Integer userId, @PathVariable("friendId") Integer friendId) throws UserValidationException, UserNotFoundException {
        userService.addFriend(userId, friendId);
        log.info("User " + friendId + " sent invite to " + userId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") Integer userId, @PathVariable("friendId") Integer friendId) throws UserValidationException, UserNotFoundException {
        userService.deleteFriend(userId, friendId);
        log.info("User " + friendId + " deleted friend " + userId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriends(@PathVariable("id") Integer id) throws UserValidationException, UserNotFoundException {
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> commonFriends(@PathVariable("id") Integer userId, @PathVariable("otherId") Integer otherUserId) throws UserValidationException, UserNotFoundException {
        return userService.commonFriends(userId, otherUserId);
    }

    @GetMapping("/{id}/recommendations")
    public Collection<Film> getRecommendations(@PathVariable("id") Integer userId) throws UserValidationException {
        return userService.getRecommendations(userId);
    }

    @PostMapping
    public User add(@Valid @RequestBody User user) {
        userService.add(user);
        log.info("Added user: {}", user);
        return user;
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) throws UserNotFoundException {
        userService.update(user);
        log.info("Updated user: {}", user);
        return user;
    }

    @DeleteMapping("{id}")
    public void deleteUserById(@PathVariable("id") Integer userId) {
        userService.deleteUserById(userId);
    }
}
