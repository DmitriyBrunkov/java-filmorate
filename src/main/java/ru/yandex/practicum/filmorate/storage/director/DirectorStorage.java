package ru.yandex.practicum.filmorate.storage.director;

import ru.yandex.practicum.filmorate.exception.DirectorNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.Collection;

public interface DirectorStorage {
    Collection<Director> getAll();

    Director get(Integer id) throws DirectorNotFoundException;

    void put(Director director);

    void update(Director director) throws DirectorNotFoundException;

    void delete(Integer id) throws DirectorNotFoundException;

}
