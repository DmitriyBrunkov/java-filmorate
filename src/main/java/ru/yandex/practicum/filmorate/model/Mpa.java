package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class Mpa {
    private Integer id;
    @Size(max = 255)
    private String name;
}
