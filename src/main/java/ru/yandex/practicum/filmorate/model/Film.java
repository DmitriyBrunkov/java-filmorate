package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validator.AfterFirstFilm;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder
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
}
