package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class AfterFirstFilmValidator implements
        ConstraintValidator<AfterFirstFilm, LocalDate> {

    @Override
    public void initialize(AfterFirstFilm contactNumber) {
    }

    @Override
    public boolean isValid(LocalDate filmDate, ConstraintValidatorContext cxt) {
        return filmDate.isAfter(Film.FIRST_FILM_DATE);
    }

}