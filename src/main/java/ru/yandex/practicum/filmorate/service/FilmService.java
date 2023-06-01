package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film get(Integer id) throws FilmNotFoundException {
        return filmStorage.get(id);
    }

    public void put(Film film) {
        filmStorage.put(film);
    }

    public void update(Film film) throws FilmValidationException, FilmNotFoundException {
        filmStorage.update(film);
    }

    public void addLike(Integer filmId, Integer userId) throws FilmValidationException {
        filmStorage.addLike(filmId, userId);
    }

    public void deleteLike(Integer filmId, Integer userId) throws FilmValidationException, FilmNotFoundException, UserNotFoundException {
        filmStorage.deleteLike(filmId, userId);
    }

    public List<Film> getPopularFilms(Integer count, Integer genreId, Integer year) {
        Genre requestGenre = new Genre();
        requestGenre.setId(genreId);
        return filmStorage.getAll().stream()
                .filter(genreId != null ? film -> film.getGenres().contains(requestGenre) : film -> true)
                .filter(year != null ? film -> film.getReleaseDate().getYear() == year : film -> true)
                .sorted(this::compare)
                .limit(count)
                .collect(Collectors.toList());
    }

    public List<Film> getCommonUserFriendFilms(int userId, int friendId) {
        return filmStorage.getAll().stream()
                .filter(film -> film.getLikes().contains(userId))
                .filter(film -> film.getLikes().contains(friendId))
                .sorted(this::compare)
                .collect(Collectors.toList());
    }

    public void deleteFilm(Integer filmId) {
        filmStorage.deleteFilm(filmId);
    }

    public List<Film> getFilmsDirectorSorted(Integer directorId, String sortBy) throws DirectorNotFoundException, InvalidParameterException {
        return filmStorage.getFilmsDirectorSorted(directorId, sortBy);
    }

    public List<Film> searchBy(String query, String by) {
        return filmStorage.searchBy(query, by).stream()
                .sorted(this::compare)
                .collect(Collectors.toList());
    }

    private int compare(Film f0, Film f1) {
        return Integer.compare(f0.getLikes().size(), f1.getLikes().size()) * (-1);
    }
}
