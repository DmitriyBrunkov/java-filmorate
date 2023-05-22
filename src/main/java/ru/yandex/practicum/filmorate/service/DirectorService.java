package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DirectorNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;

import java.util.Collection;

@Service
public class DirectorService {
    private final DirectorStorage directorStorage;

    @Autowired
    public DirectorService(DirectorStorage directorStorage){
        this.directorStorage = directorStorage;
    }

    public Collection<Director> getAll(){
        return directorStorage.getAll();
    }

    public Director get(Integer id) throws DirectorNotFoundException {
        return directorStorage.get(id);
    }

    public void put(Director director){
        directorStorage.put(director);
    }

    public void update(Director director) throws DirectorNotFoundException {
        directorStorage.update(director);
    }

    public void delete(Integer id) throws DirectorNotFoundException {
        directorStorage.delete(id);
    }
}
