package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("InMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {
    private static int currentId = 1;
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Collection<Film> getAll() {
        return films.values();
    }

    @Override
    public Film get(Integer id) throws FilmNotFoundException {
        if (!films.containsKey(id)) {
            throw new FilmNotFoundException("ID: " + id + " doesn't exist");
        }
        return films.get(id);
    }

    @Override
    public void put(Film film) {
        film.setId(generateId());
        films.put(film.getId(), film);
    }

    @Override
    public void update(Film film) throws FilmNotFoundException {
        if (film.getId() == null || !films.containsKey(film.getId())) {
            throw new FilmNotFoundException("ID: " + film.getId() + " doesn't exist");
        }
        films.put(film.getId(), film);
    }

    @Override
    public void addLike(Integer filmId, Integer userId) throws FilmValidationException {
        if (filmId == null || !films.containsKey(filmId)) {
            throw new FilmValidationException("ID: " + filmId + " doesn't exist");
        }
        films.get(filmId).getLikes().add(userId);
    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) throws FilmNotFoundException, UserNotFoundException {
        if (filmId == null || !films.containsKey(filmId)) {
            throw new FilmNotFoundException("ID: " + filmId + " doesn't exist");
        } else if (!films.get(filmId).getLikes().contains(userId)) {
            throw new UserNotFoundException("User ID: " + userId + " doesn't exist");
        }
        films.get(filmId).getLikes().remove(userId);
    }

    @Override
    public List<Film> getFilmsDirectorSorted(Integer directorId, String sortBy) throws DirectorNotFoundException {
        throw new NotImplException("InMemoryFilmStorage: getFilmsDirectorSorted() not implemented");
    }

    @Override
    public void deleteFilm(Integer filmId) {
        films.remove(filmId);
    }

    @Override
    public List<Film> searchBy(String query, String by) {
        throw new NotImplException("InMemoryFilmStorage: searchBy() not implemented");
    }

    private int generateId() {
        while (films.containsKey(currentId)) {
            currentId++;
        }
        return currentId++;
    }
}
