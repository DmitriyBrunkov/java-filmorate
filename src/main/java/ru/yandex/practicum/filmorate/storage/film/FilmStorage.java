package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {
    Collection<Film> getAll();

    Film get(Integer id) throws FilmNotFoundException;

    void put(Film film);

    void update(Film film) throws FilmValidationException, FilmNotFoundException;

    void addLike(Integer filmId, Integer userId) throws FilmValidationException;

    void deleteLike(Integer filmId, Integer userId) throws FilmValidationException, FilmNotFoundException, UserNotFoundException;

    List<Film> getFilmsDirectorSorted(Integer directorId, String sortBy) throws DirectorNotFoundException, InvalidParameterException;

    List<Film> searchBy(String query, String by);
}
