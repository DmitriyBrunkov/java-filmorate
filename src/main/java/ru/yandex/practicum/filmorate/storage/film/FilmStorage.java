package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.FilmValidationException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Collection<Film> getAll();

    Film get(Integer id) throws FilmNotFoundException;

    void put(Film film);

    void update(Film film) throws FilmValidationException, FilmNotFoundException;

    void addLike(Integer filmId, Integer userId) throws FilmValidationException;

    void deleteLike(Integer filmId, Integer userId) throws FilmValidationException, FilmNotFoundException, UserNotFoundException;
}
