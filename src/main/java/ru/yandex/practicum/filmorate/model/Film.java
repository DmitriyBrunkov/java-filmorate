package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.validator.AfterFirstFilm;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Data
public class Film {
    public static final LocalDate FIRST_FILM_DATE = LocalDate.of(1895, 12, 28);
    private Integer id;
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    @AfterFirstFilm
    private LocalDate releaseDate;
    @Positive
    private Integer duration;
    private Mpa mpa;
    private Set<Integer> likes = new HashSet<>();
    private Set<Genre> genres = new TreeSet<>();
    private Set<Director> directors = new HashSet<>();
}
