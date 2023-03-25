package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private static int currentId = 1;

    @GetMapping
    public Collection<User> getAll() {
        return users.values();
    }

    @PostMapping
    public User add(@Valid @RequestBody User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        user.setId(generateId());
        users.put(user.getId(), user);
        log.info("Added user: {}", user);
        return user;
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) throws UserValidationException {
        if (user.getId() == null || !users.containsKey(user.getId())) {
            throw new UserValidationException("ID: " + user.getId() + " doesn't exist");
        }
        users.put(user.getId(), user);
        log.info("Updated user: {}", user);
        return user;
    }
    private int generateId() {
        while (users.containsKey(currentId)) {
            currentId++;
        }
        return currentId++;
    }
}
