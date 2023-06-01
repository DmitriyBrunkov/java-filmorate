package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.DirectorNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@Slf4j
@RequestMapping("/directors")
public class DirectorController {
    private final DirectorService directorService;

    @Autowired
    public DirectorController(DirectorService directorService) {
        this.directorService = directorService;
    }

    @GetMapping
    public Collection<Director> getAll() {
        return directorService.getAll();
    }

    @GetMapping("{id}")
    public Director get(@PathVariable("id") Integer id) throws DirectorNotFoundException {
        return directorService.get(id);
    }

    @PostMapping
    public Director add(@Valid @RequestBody Director director) {
        directorService.put(director);
        log.info("Added director: {}", director);
        return director;
    }

    @PutMapping
    public Director put(@Valid @RequestBody Director director) throws DirectorNotFoundException {
        directorService.update(director);
        log.info("Updated director: {}", director);
        return director;
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Integer id) throws DirectorNotFoundException {
        directorService.delete(id);
        log.info("Delete director by id: {}", id);
    }
}