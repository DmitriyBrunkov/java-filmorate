package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.DataBinder;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FilmControllerTest {

    @Autowired
    private FilmController filmController;

    @Autowired
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

    @Autowired
    Validator validator = factory.getValidator();

    @Test
    public void shouldAddFilm() {
        Film film = new Film();
        film.setName("Pulp Fiction");
        film.setDescription("adipisicing");
        film.setReleaseDate(LocalDate.of(1967, 3, 25));
        film.setDuration(100);
        Film getFilm = filmController.add(film);
        Assertions.assertEquals(film.getId(), getFilm.getId());
        Assertions.assertEquals(film.getName(), getFilm.getName());
    }

    @Test
    public void shouldThrowExceptionWhenPutNonExistentId() {
        Film film = new Film();
        film.setId(1234);
        film.setName("Pulp Fiction");
        film.setDescription("adipisicing");
        film.setReleaseDate(LocalDate.of(1967, 3, 25));
        film.setDuration(100);
        final FilmNotFoundException exception = assertThrows(FilmNotFoundException.class,
                () -> filmController.put(film)
        );
        assertEquals("ID: " + film.getId() + " doesn't exist", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenNameIsBlank() {
        Film film = new Film();
        film.setName("");
        film.setDescription("adipisicing");
        film.setReleaseDate(LocalDate.of(1967, 3, 25));
        film.setDuration(100);
        final DataBinder dataBinder = new DataBinder(film);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void shouldThrowExceptionWhenDescriptionIsMore200Chars() {
        Film film = new Film();
        film.setName("Pulp Fiction");
        film.setDescription("Pulp Fiction".repeat(200));
        film.setReleaseDate(LocalDate.of(1967, 3, 25));
        film.setDuration(100);
        final DataBinder dataBinder = new DataBinder(film);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void shouldThrowExceptionWhenReleaseIsBeforeFirstFilmEver() {
        Film film = new Film();
        film.setName("Pulp Fiction");
        film.setDescription("Pulp Fiction");
        film.setReleaseDate(LocalDate.of(967, 3, 25));
        film.setDuration(100);
        final DataBinder dataBinder = new DataBinder(film);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void shouldThrowExceptionWhenDurationIsNegative() {
        Film film = new Film();
        film.setName("Pulp Fiction");
        film.setDescription("Pulp Fiction");
        film.setReleaseDate(LocalDate.of(1967, 3, 25));
        film.setDuration(-100);
        final DataBinder dataBinder = new DataBinder(film);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }
}
