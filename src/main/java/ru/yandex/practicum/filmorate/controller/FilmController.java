package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> getAll() {
        return filmService.getAll();
    }

    @PostMapping
    public Film add(@Valid @RequestBody Film film) {
        filmService.put(film);
        log.info("Added film: {}", film);
        return film;
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film) throws FilmValidationException, FilmNotFoundException {
        filmService.update(film);
        log.info("Updated film: {}", film);
        return film;
    }

    @GetMapping("/{id}")
    public Film get(@PathVariable("id") Integer id) throws FilmNotFoundException {
        return filmService.get(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") Integer filmId, @PathVariable("userId") Integer userId) throws FilmValidationException {
        filmService.addLike(filmId, userId);
        log.info("User {} liked film {}", userId, filmId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") Integer filmId, @PathVariable("userId") Integer userId) throws FilmValidationException, FilmNotFoundException, UserNotFoundException {
        filmService.deleteLike(filmId, userId);
        log.info("User {} deleted like from film {}", userId, filmId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") Integer count, @RequestParam(required = false) Integer genreId, @RequestParam(required = false) Integer year) {
        return filmService.getPopularFilms(count, genreId, year);
    }

    @GetMapping("/common")
    public List<Film> getCommonUserFriendFilms(@RequestParam int userId, @RequestParam int friendId) {
        return filmService.getCommonUserFriendFilms(userId, friendId);
    }

    @GetMapping("director/{directorId}")
    public List<Film> getDirectorSortedFilms(@PathVariable Integer directorId, @RequestParam String sortBy) throws DirectorNotFoundException, InvalidParameterException {
        return filmService.getFilmsDirectorSorted(directorId, sortBy);
    }

    @DeleteMapping("{id}")
    public void deleteFilm(@PathVariable("id") int filmId) {
        filmService.deleteFilm(filmId);
    }

    @GetMapping("/search")
    public List<Film> search(@RequestParam(defaultValue = "") String query, @RequestParam(defaultValue = "") String by) {
        return filmService.searchBy(query, by);
    }
}
