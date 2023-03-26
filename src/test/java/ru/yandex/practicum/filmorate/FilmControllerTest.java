package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.DataBinder;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.FilmValidationException;
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
        Film film = Film.builder()
                .name("Pulp Fiction")
                .description("adipisicing")
                .releaseDate(LocalDate.of(1967, 3, 25))
                .duration(100)
                .build();
        filmController.add(film);
        for (Film filmFromCollection : filmController.getAll()) {
            if (filmFromCollection.getId().equals(film.getId())) {
                Assertions.assertEquals(film, filmFromCollection);
            }
        }
    }

    @Test
    public void shouldThrowExceptionWhenPutNonExistentId() {
        Film film = Film.builder()
                .id(1234)
                .name("Pulp Fiction")
                .description("adipisicing")
                .releaseDate(LocalDate.of(1967, 3, 25))
                .duration(100)
                .build();
        final FilmValidationException exception = assertThrows(FilmValidationException.class,
                () -> filmController.put(film)
        );
        assertEquals("ID: " + film.getId() + " doesn't exist", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenNameIsBlank() {
        Film film = Film.builder()
                .name("")
                .description("adipisicing")
                .releaseDate(LocalDate.of(1967, 3, 25))
                .duration(100)
                .build();
        final DataBinder dataBinder = new DataBinder(film);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void shouldThrowExceptionWhenDescriptionIsMore200Chars() {
        Film film = Film.builder()
                .name("Pulp Fiction")
                .description("Pulp Fiction".repeat(200))
                .releaseDate(LocalDate.of(1967, 3, 25))
                .duration(100)
                .build();
        final DataBinder dataBinder = new DataBinder(film);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void shouldThrowExceptionWhenReleaseIsBeforeFirstFilmEver() {
        Film film = Film.builder()
                .name("Pulp Fiction")
                .description("Pulp Fiction")
                .releaseDate(LocalDate.of(967, 3, 25))
                .duration(100)
                .build();
        final DataBinder dataBinder = new DataBinder(film);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void shouldThrowExceptionWhenDurationIsNegative() {
        Film film = Film.builder()
                .name("Pulp Fiction")
                .description("Pulp Fiction")
                .releaseDate(LocalDate.of(1967, 3, 25))
                .duration(-100)
                .build();
        final DataBinder dataBinder = new DataBinder(film);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }
}
