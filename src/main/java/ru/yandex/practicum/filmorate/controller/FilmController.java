package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private static int currentId = 1;
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getAll() {
        return films.values();
    }

    @PostMapping
    public Film add(@Valid @RequestBody Film film) {
        film.setId(generateId());
        films.put(film.getId(), film);
        log.info("Added film: {}", film);
        return film;
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film) throws FilmValidationException {
        if (film.getId() == null || !films.containsKey(film.getId())) {
            throw new FilmValidationException("ID: " + film.getId() + " doesn't exist");
        }
        films.put(film.getId(), film);
        log.info("Updated film: {}", film);
        return film;
    }

    private int generateId() {
        while (films.containsKey(currentId)) {
            currentId++;
        }
        return currentId++;
    }
}
